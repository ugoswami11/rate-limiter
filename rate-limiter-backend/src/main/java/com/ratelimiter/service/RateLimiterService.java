package com.ratelimiter.service;

import com.ratelimiter.dto.RateLimiterResponse;
import com.ratelimiter.model.RateLimitResult;
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

        RateLimitResult result =
                strategy.allowRequest(userId);

        String message;

        if (result.isAllowed()) {
            message = "Request allowed";
        } else {
            message = "Rate limit exceeded";
        }

        return new RateLimiterResponse(
                result.isAllowed(),
                result.getRemainingTokens(),
                message
        );
    }
}