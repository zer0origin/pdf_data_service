package com.w.callum.pdf_service_data.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(new errorReporting(ex.getMessage(), ex));
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<?> handleTimeoutException(Exception ex) {
        return ResponseEntity.status(500).body(new errorTimeout("Request timed out!"));
    }

    public record errorReporting(String message, Exception exception) { }
    public record errorTimeout(String message) { }
}
