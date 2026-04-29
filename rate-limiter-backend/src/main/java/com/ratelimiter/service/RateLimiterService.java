package com.ratelimiter.service;

import com.ratelimiter.strategy.RateLimiterStrategy;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private final RateLimiterStrategy strategy;

    public RateLimiterService(RateLimiterStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean isAllowed(String userId) {
        return strategy.allowRequest(userId);
    }
}
