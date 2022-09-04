package com.techsolutio.products.controller.advice;

import com.techsolutio.products.dto.ResponseError;
import com.techsolutio.products.exception.HttpResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HttpResponseExceptionHandler {

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<ResponseError> handler(HttpResponseException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(exception.getError());
    }
}
