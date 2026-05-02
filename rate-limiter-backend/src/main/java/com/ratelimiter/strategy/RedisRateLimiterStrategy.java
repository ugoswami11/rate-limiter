package com.ratelimiter.strategy;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisRateLimiterStrategy implements RateLimiterStrategy {

    private final RedisTemplate<String, Object> redisTemplate;

    private final int capacity = 5;
    private final int refillRate = 1; // tokens per second

    public RedisRateLimiterStrategy(
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean allowRequest(String userId) {

        String tokenKey = "rate_limit:" + userId + ":tokens";
        String lastRefillKey = "rate_limit:" + userId + ":last_refill";

        Long currentTime = System.currentTimeMillis();

        Object tokenObj =
                redisTemplate.opsForValue().get(tokenKey);

        Object lastRefillObj =
                redisTemplate.opsForValue().get(lastRefillKey);

        int tokens;
        long lastRefillTime;

        /*
         * First request for this user
         */
        if (tokenObj == null || lastRefillObj == null) {
            tokens = capacity;
            lastRefillTime = currentTime;
        } else {
            tokens = Integer.parseInt(tokenObj.toString());
            lastRefillTime =
                    Long.parseLong(lastRefillObj.toString());
        }

        /*
         * Refill logic
         */
        long elapsedTime =
                (currentTime - lastRefillTime) / 1000;

        if (elapsedTime > 0) {
            int tokensToAdd =
                    (int) (elapsedTime * refillRate);

            tokens = Math.min(
                    capacity,
                    tokens + tokensToAdd
            );

            lastRefillTime = currentTime;
        }

        /*
         * Allow or block request
         */
        if (tokens > 0) {
            tokens--;

            redisTemplate.opsForValue()
                    .set(tokenKey, tokens);

            redisTemplate.opsForValue()
                    .set(lastRefillKey, lastRefillTime);

            return true;
        }

        /*
         * Save current state even if blocked
         */
        redisTemplate.opsForValue()
                .set(tokenKey, tokens);

        redisTemplate.opsForValue()
                .set(lastRefillKey, lastRefillTime);

        return false;
    }
}