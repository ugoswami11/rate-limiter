package com.ratelimiter.service;

import com.ratelimiter.dto.RateLimiterResponse;
import com.ratelimiter.strategy.RedisRateLimiterStrategy;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final RedisRateLimiterStrategy strategy;

    public RateLimiterService(
            RedisRateLimiterStrategy strategy
    ) {
        this.strategy = strategy;
    }

    public RateLimiterResponse checkRateLimit(String userId) {

        boolean allowed =
                strategy.allowRequest(userId);

        String message;
        int remainingTokens = -1; // optional improvement later

        if (allowed) {
            message = "Request allowed";
        } else {
            message = "Rate limit exceeded";
        }

        return new RateLimiterResponse(
                allowed,
                remainingTokens,
                message
        );
    }
}