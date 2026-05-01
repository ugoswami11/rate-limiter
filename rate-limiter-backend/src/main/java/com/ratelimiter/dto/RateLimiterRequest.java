package com.ratelimiter.dto;

import jakarta.validation.constraints.NotBlank;

public class RateLimiterRequest {

    @NotBlank(message = "userId must not be blank")
    private String userId;

    public RateLimiterRequest(){
    }

    public RateLimiterRequest(String userId){
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
