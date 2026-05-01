package com.ratelimiter.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private String status;
    private String error;
    private String message;

    public ErrorResponse(){

    }

    public ErrorResponse(LocalDateTime timestamp, String status, String error, String message){
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message=message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
