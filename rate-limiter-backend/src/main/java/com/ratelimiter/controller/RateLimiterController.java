package com.ratelimiter.controller;

import com.ratelimiter.dto.RateLimiterRequest;
import com.ratelimiter.dto.RateLimiterResponse;
import com.ratelimiter.service.RateLimiterService;
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
    public ResponseEntity<RateLimiterResponse> checkRateLimit(@RequestBody RateLimiterRequest req){
        boolean allowed = service.isAllowed(req.getUserId());
        RateLimiterResponse response = new RateLimiterResponse(allowed);
        return ResponseEntity.ok(response);
    }
}
