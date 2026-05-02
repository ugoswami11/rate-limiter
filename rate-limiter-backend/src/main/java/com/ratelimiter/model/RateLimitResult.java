package com.ratelimiter.model;

public class RateLimitResult {

    private boolean allowed;
    private int remainingTokens;

    public RateLimitResult() {
    }

    public RateLimitResult(boolean allowed, int remainingTokens) {
        this.allowed = allowed;
        this.remainingTokens = remainingTokens;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public int getRemainingTokens() {
        return remainingTokens;
    }

    public void setRemainingTokens(int remainingTokens) {
        this.remainingTokens = remainingTokens;
    }
}