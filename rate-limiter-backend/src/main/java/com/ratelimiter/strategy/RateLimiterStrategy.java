package com.ratelimiter.strategy;

public interface RateLimiterStrategy {

    boolean allowRequest(String userId);

}
