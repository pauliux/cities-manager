package com.application.cities.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static org.springframework.http.ResponseEntity.badRequest;

@RestControllerAdvice
public class ErrorHandlingAdvice {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        return badRequest().body(Map.of("error", ex.getMessage()));
    }
}
