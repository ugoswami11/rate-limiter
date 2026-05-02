package com.ratelimiter.strategy;

import com.ratelimiter.model.RateLimitResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedisRateLimiterStrategyTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private RedisRateLimiterStrategy strategy;

    private String userId;

    @BeforeEach
    void setUp() {
        userId = "john123";
    }

    @Test
    void shouldAllowRequestWhenTokensAvailable() {

        /*
         * Arrange
         *
         * Lua script returns:
         * [allowed, remainingTokens]
         *
         * Example:
         * [1, 4]
         */
        List<Long> luaResponse =
                Arrays.asList(1L, 4L);

        when(redisTemplate.execute(
                any(),
                anyList(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(luaResponse);

        /*
         * Act
         */
        RateLimitResult result =
                strategy.allowRequest(userId);

        /*
         * Assert
         */
        assertNotNull(result);
        assertTrue(result.isAllowed());
        assertEquals(
                4,
                result.getRemainingTokens()
        );
    }

    @Test
    void shouldBlockRequestWhenNoTokensAvailable() {

        /*
         * Arrange
         *
         * Lua script returns:
         * [0, 0]
         */
        List<Long> luaResponse =
                Arrays.asList(0L, 0L);

        when(redisTemplate.execute(
                any(),
                anyList(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(luaResponse);

        /*
         * Act
         */
        RateLimitResult result =
                strategy.allowRequest(userId);

        /*
         * Assert
         */
        assertNotNull(result);
        assertFalse(result.isAllowed());
        assertEquals(
                0,
                result.getRemainingTokens()
        );
    }

    @Test
    void shouldThrowExceptionWhenLuaReturnsInvalidResponse() {

        /*
         * Arrange
         *
         * Invalid response example:
         * null
         */
        when(redisTemplate.execute(
                any(),
                anyList(),
                anyString(),
                anyString(),
                anyString()
        )).thenReturn(null);

        /*
         * Act + Assert
         */
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> strategy.allowRequest(userId)
                );

        assertEquals(
                "Invalid Redis Lua script response",
                exception.getMessage()
        );
    }
}