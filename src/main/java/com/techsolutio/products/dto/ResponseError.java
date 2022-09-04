package com.techsolutio.products.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResponseError {
    private Integer status;
    private String message;
    private String reason;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    private void setStatus(Integer status) {
        this.status = status;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private void setReason(String reason) {
        this.reason = reason;
    }

    public static class Builder {
        private HttpStatus status;
        private String message;

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseError build() {
            ResponseError responseError = new ResponseError();

            responseError.setStatus(status.value());
            responseError.setReason(status.getReasonPhrase());
            responseError.setMessage(message);

            return responseError;
        }
    }
}
