package com.ratelimiter.service;

import com.ratelimiter.dto.RateLimiterResponse;
import com.ratelimiter.model.TokenBucket;
import com.ratelimiter.strategy.RateLimiterStrategy;
import com.ratelimiter.strategy.TokenBucketStrategy;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final TokenBucketStrategy strategy;

    public RateLimiterService(TokenBucketStrategy strategy) {
        this.strategy = strategy;
    }

    public RateLimiterResponse checkRateLimit(String userId){
        TokenBucket bucket = strategy.getBucket(userId);

        boolean allowed = bucket.allowRequest();
        int remainingTokens = bucket.getRemainingTokens();

        String message;

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
