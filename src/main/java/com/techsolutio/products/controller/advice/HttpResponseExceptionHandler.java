package com.techsolutio.products.controller.advice;

import com.techsolutio.products.dto.ResponseError;
import com.techsolutio.products.exception.HttpResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HttpResponseExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(HttpResponseExceptionHandler.class);

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<ResponseError> handler(HttpResponseException exception) {
        log.warn("REQUEST FAIL, STATUS CODE {}.", exception.getStatusCode());

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(exception.getError());
    }
}
