package strategy;

import model.TokenBucket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketStrategy implements  RateLimiterStrategy{

    private ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    private final int capacity;
    private final int refillRate;

    public TokenBucketStrategy(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    @Override
    public boolean allowRequest(String userId) {

        TokenBucket bucket = buckets.computeIfAbsent(
                userId,
                k -> new TokenBucket(capacity, refillRate)
        );

        return bucket.allowRequest();
    }
}
