package com.ratelimiter.dto;

public class RateLimiterResponse {

    private boolean allowed;

    public RateLimiterResponse(){
    }

    public RateLimiterResponse(boolean allowed){
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}
