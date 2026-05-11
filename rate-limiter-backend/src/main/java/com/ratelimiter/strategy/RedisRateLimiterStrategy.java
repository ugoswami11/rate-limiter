package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimitResult;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RedisRateLimiterStrategy implements RateLimiterStrategy {

    private final RedisTemplate<String, Object> redisTemplate;

    private final int capacity = 5;
    private final int refillRate = 1;

    private final DefaultRedisScript<List> rateLimiterScript;

    public RedisRateLimiterStrategy(
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;

        /*
         * Load Lua script from resources/scripts/rate_limiter.lua
         */
        this.rateLimiterScript = new DefaultRedisScript<>();
        this.rateLimiterScript.setScriptSource(
                new ResourceScriptSource(
                        new ClassPathResource(
                                "scripts/rate-limiter.lua"
                        )
                )
        );

        this.rateLimiterScript.setResultType(List.class);
    }

    @Override
    public RateLimitResult allowRequest(String userId) {

        String tokenKey =
                "rate_limit:" + userId + ":tokens";

        String lastRefillKey =
                "rate_limit:" + userId + ":last_refill";

        long currentTime =
                System.currentTimeMillis();

        /*
         * KEYS passed to Lua script
         */
        List<String> keys = Arrays.asList(
                tokenKey,
                lastRefillKey
        );

        /*
         * ARGV passed to Lua script
         *
         * ARGV[1] = capacity
         * ARGV[2] = refillRate
         * ARGV[3] = currentTime
         */
        List result = redisTemplate.execute(
                rateLimiterScript,
                keys,
                String.valueOf(capacity),
                String.valueOf(refillRate),
                String.valueOf(currentTime)
        );

        /*
         * Lua returns:
         * [allowed, remainingTokens]
         *
         * Example:
         * [1, 4]
         */
        if (result == null || result.size() < 2) {
            throw new RuntimeException(
                    "Invalid Redis Lua script response"
            );
        }

        int allowedValue =
                Integer.parseInt(result.get(0).toString());

        int remainingTokens =
                Integer.parseInt(result.get(1).toString());

        boolean allowed =
                allowedValue == 1;

        return new RateLimitResult(
                allowed,
                remainingTokens
        );
    }
}