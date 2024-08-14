package com.blogilf.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> responseEntityBuilder(HttpStatus status, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);

        return new ResponseEntity<>(body, status);
    }

    // 400
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleCustomBadRequestException(CustomBadRequestException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return responseEntityBuilder(HttpStatus.BAD_REQUEST, ex.getMessage(), path);
    }

    // 404
    @ExceptionHandler(CustomResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomResourceNotFoundException(CustomResourceNotFoundException ex, WebRequest request) {        
        String path = request.getDescription(false).replace("uri=", "");
        return responseEntityBuilder(HttpStatus.NOT_FOUND, ex.getMessage(), path);
    }

    // 500
    @ExceptionHandler(CustomInternalServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleCustomInternalServerErrorException(CustomInternalServerErrorException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return responseEntityBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), path);
    }
}
