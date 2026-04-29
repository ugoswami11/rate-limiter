package com.ratelimiter.dto;

public class RateLimiterRequest {

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
