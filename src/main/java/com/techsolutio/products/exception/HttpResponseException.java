package com.techsolutio.products.exception;

import com.techsolutio.products.dto.ResponseError;
import org.springframework.http.HttpStatus;

public class HttpResponseException extends RuntimeException {
    HttpStatus httpStatus;
    String message;

    public HttpResponseException(HttpStatus httpStatus, String message) {
        super(message);

        this.httpStatus = httpStatus;
        this.message = message;
    }

    public Integer getStatusCode() {
        return httpStatus.value();
    }

    public ResponseError getError() {
        return new ResponseError.Builder()
                .status(this.httpStatus)
                .message(message)
                .build();
    }
}
