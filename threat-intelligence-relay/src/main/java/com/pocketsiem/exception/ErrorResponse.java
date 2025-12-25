package com.pocketsiem.exception;

public class ErrorResponse {
    private Integer status;
    private String message;
    private Long timestamp;

    public ErrorResponse() {}

    public ErrorResponse(Integer status, String message, Long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Integer status;
        private String message;
        private Long timestamp;

        public Builder status(Integer status) { this.status = status; return this; }
        public Builder message(String message) { this.message = message; return this; }
        public Builder timestamp(Long timestamp) { this.timestamp = timestamp; return this; }

        public ErrorResponse build() {
            return new ErrorResponse(status, message, timestamp);
        }
    }
}
