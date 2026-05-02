package com.ratelimiter.controller;

import com.ratelimiter.dto.RateLimiterRequest;
import com.ratelimiter.dto.RateLimiterResponse;
import com.ratelimiter.service.RateLimiterService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RateLimiterController.class)
class RateLimiterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RateLimiterService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllowedResponse() throws Exception {

        /*
         * Arrange
         */
        RateLimiterRequest request =
                new RateLimiterRequest("john123");

        RateLimiterResponse mockResponse =
                new RateLimiterResponse(
                        true,
                        4,
                        "Request allowed"
                );

        when(service.checkRateLimit("john123"))
                .thenReturn(mockResponse);

        /*
         * Act + Assert
         */
        mockMvc.perform(
                        post("/api/rate-limit/check")
                                .contentType(
                                        String.valueOf(MediaType.APPLICATION_JSON)
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed")
                        .value(true))
                .andExpect(jsonPath("$.remainingTokens")
                        .value(4))
                .andExpect(jsonPath("$.message")
                        .value("Request allowed"));
    }

    @Test
    void shouldReturnValidationErrorWhenUserIdIsBlank()
            throws Exception {

        /*
         * Arrange
         */
        RateLimiterRequest request =
                new RateLimiterRequest("");

        /*
         * Act + Assert
         */
        mockMvc.perform(
                        post("/api/rate-limit/check")
                                .contentType(
                                        String.valueOf(MediaType.APPLICATION_JSON)
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.error")
                        .value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("userId must not be blank"));
    }
}