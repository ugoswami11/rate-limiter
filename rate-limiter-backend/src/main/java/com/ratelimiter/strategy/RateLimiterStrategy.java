package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimitResult;

public interface RateLimiterStrategy {

    RateLimitResult allowRequest(String userId);

}
