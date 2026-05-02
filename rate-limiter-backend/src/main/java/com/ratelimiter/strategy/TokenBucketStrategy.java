package com.ratelimiter.strategy;

import com.ratelimiter.model.TokenBucket;
import com.ratelimiter.strategy.RateLimiterStrategy;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBucketStrategy  {

//    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
//
//    private final int capacity = 5;
//    private final int refillRate =  1;
//
//    @Override
//    public boolean allowRequest(String userId) {
//        return getBucket(userId).allowRequest();
//    }
//
//    public TokenBucket getBucket(String userId) {
//        return buckets.computeIfAbsent(
//                userId,
//                k -> new TokenBucket(capacity, refillRate)
//        );
//    }
}
