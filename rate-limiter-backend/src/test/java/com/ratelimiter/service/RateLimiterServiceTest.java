package com.ratelimiter.service;

import com.ratelimiter.dto.RateLimiterResponse;
import com.ratelimiter.model.RateLimitResult;
import com.ratelimiter.strategy.RedisRateLimiterStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimiterServiceTest {

    @Mock
    private RedisRateLimiterStrategy strategy;

    @InjectMocks
    private RateLimiterService service;

    private String userId;

    @BeforeEach
    void setUp() {
        userId = "john123";
    }

    @Test
    void shouldAllowRequestSuccessfully() {

        /*
         * Arrange
         */
        RateLimitResult mockResult =
                new RateLimitResult(true, 4);

        when(strategy.allowRequest(userId))
                .thenReturn(mockResult);

        /*
         * Act
         */
        RateLimiterResponse response =
                service.checkRateLimit(userId);

        /*
         * Assert
         */
        assertNotNull(response);

        assertTrue(response.isAllowed());
        assertEquals(4, response.getRemainingTokens());
        assertEquals(
                "Request allowed",
                response.getMessage()
        );
    }

    @Test
    void shouldBlockRequestWhenRateLimitExceeded() {

        /*
         * Arrange
         */
        RateLimitResult mockResult =
                new RateLimitResult(false, 0);

        when(strategy.allowRequest(userId))
                .thenReturn(mockResult);

        /*
         * Act
         */
        RateLimiterResponse response =
                service.checkRateLimit(userId);

        /*
         * Assert
         */
        assertNotNull(response);

        assertFalse(response.isAllowed());
        assertEquals(0, response.getRemainingTokens());
        assertEquals(
                "Rate limit exceeded",
                response.getMessage()
        );
    }
}