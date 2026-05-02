package com.ratelimiter.service;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTestService(
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void testRedisConnection() {

        String key = "rate_limit:test_user";

        redisTemplate.opsForValue().set(key, 5);

        Object value = redisTemplate.opsForValue().get(key);

        System.out.println("Redis test value: " + value);
    }
}