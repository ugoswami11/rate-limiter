package com.ratelimiter.integration;


import com.ratelimiter.dto.RateLimiterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RateLimiterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_URL =
            "/api/rate-limit/check";

    @BeforeEach
    void setUp() {

        /*
         * Clean Redis before every test
         * to avoid old data affecting test results
         */
        redisTemplate.getConnectionFactory()
                .getConnection()
                .flushAll();
    }

    @Test
    void shouldAllowRequestsUntilLimitExceeded()
            throws Exception {

        /*
         * Arrange
         */
        RateLimiterRequest request =
                new RateLimiterRequest("john123");

        String requestBody =
                objectMapper.writeValueAsString(request);

        /*
         * First 5 requests should be allowed
         */
        for (int i = 1; i <= 5; i++) {

            mockMvc.perform(
                            post(API_URL)
                                    .contentType(
                                            String.valueOf(MediaType.APPLICATION_JSON)
                                    )
                                    .content(requestBody)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.allowed")
                            .value(true));
        }

        /*
         * 6th request should be blocked
         */
        mockMvc.perform(
                        post(API_URL)
                                .contentType(
                                        String.valueOf(MediaType.APPLICATION_JSON)
                                )
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed")
                        .value(false))
                .andExpect(jsonPath("$.remainingTokens")
                        .value(0))
                .andExpect(jsonPath("$.message")
                        .value("Rate limit exceeded"));
    }

    @Test
    void shouldRefillTokensAfterWaiting()
            throws Exception, InterruptedException {

        /*
         * Arrange
         */
        RateLimiterRequest request =
                new RateLimiterRequest("john456");

        String requestBody =
                objectMapper.writeValueAsString(request);

        /*
         * Exhaust all 5 tokens
         */
        for (int i = 1; i <= 5; i++) {
            mockMvc.perform(
                    post(API_URL)
                            .contentType(
                                    String.valueOf(MediaType.APPLICATION_JSON)
                            )
                            .content(requestBody)
            );
        }

        /*
         * Next request should fail
         */
        mockMvc.perform(
                        post(API_URL)
                                .contentType(
                                        String.valueOf(MediaType.APPLICATION_JSON)
                                )
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed")
                        .value(false));

        /*
         * Wait for refill
         *
         * refillRate = 1 token/sec
         *
         * Wait 3 seconds → should refill 3 tokens
         */
        Thread.sleep(3000);

        /*
         * Request should pass again
         */
        mockMvc.perform(
                        post(API_URL)
                                .contentType(
                                        String.valueOf(MediaType.APPLICATION_JSON)
                                )
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed")
                        .value(true));
    }
}