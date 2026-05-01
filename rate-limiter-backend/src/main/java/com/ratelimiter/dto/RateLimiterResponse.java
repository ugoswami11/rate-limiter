package com.ratelimiter.dto;

public class RateLimiterResponse {

    private boolean allowed;
    private int remainingTokens;
    private String message;

    public RateLimiterResponse(){
    }

    public RateLimiterResponse(boolean allowed, int remainingTokens, String message){
        this.allowed = allowed;
        this.remainingTokens = remainingTokens;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRemainingTokens() {
        return remainingTokens;
    }

    public void setRemainingTokens(int remainingTokens) {
        this.remainingTokens = remainingTokens;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

}
