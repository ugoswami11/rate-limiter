package com.ratelimiter.controller;

import com.ratelimiter.dto.RateLimiterRequest;
import com.ratelimiter.dto.RateLimiterResponse;
import com.ratelimiter.service.RateLimiterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rate-limit")
public class RateLimiterController {

    public RateLimiterService service;

    public RateLimiterController(RateLimiterService service){
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseEntity<RateLimiterResponse> checkRateLimit(@Valid @RequestBody RateLimiterRequest request){
        RateLimiterResponse response =
                service.checkRateLimit(request.getUserId());

        return ResponseEntity.ok(response);
    }
}
