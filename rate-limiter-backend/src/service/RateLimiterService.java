package service;

import strategy.RateLimiterStrategy;

public class RateLimiterService {

    private final RateLimiterStrategy strategy;

    public RateLimiterService(RateLimiterStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean isAllowed(String userId) {
        return strategy.allowRequest(userId);
    }
}
