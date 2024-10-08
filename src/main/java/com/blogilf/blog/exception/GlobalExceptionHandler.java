package com.blogilf.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> responseEntityBuilder(HttpStatus status, String message, String path, Map<String, String> errors) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());

        if (errors != null) {
            body.put("errors", errors);
        }

        body.put("message", message);
        body.put("path", path);

        return new ResponseEntity<>(body, status);
    }

    // 400
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleCustomBadRequestException(CustomBadRequestException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return responseEntityBuilder(HttpStatus.BAD_REQUEST, ex.getMessage(), path,null);
    }

    // 401
    @ExceptionHandler(CustomUnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleCustomUnauthorizedException(CustomUnauthorizedException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return responseEntityBuilder(HttpStatus.UNAUTHORIZED, ex.getMessage(), path,null);
    }
    
    // 403
    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleCustomForbiddenException(CustomForbiddenException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return responseEntityBuilder(HttpStatus.FORBIDDEN, ex.getMessage(), path,null);
    }

    // 404
    @ExceptionHandler(CustomResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomResourceNotFoundException(CustomResourceNotFoundException ex, WebRequest request) {        
        String path = request.getDescription(false).replace("uri=", "");
        return responseEntityBuilder(HttpStatus.NOT_FOUND, ex.getMessage(), path,null);
    }

    // 409
    @ExceptionHandler(CustomConflictException.class)
    public ResponseEntity<Map<String, Object>> handleCustomConflictException(CustomConflictException ex, WebRequest request) {        
        String path = request.getDescription(false).replace("uri=", "");
        return responseEntityBuilder(HttpStatus.CONFLICT, ex.getMessage(), path,null);
    }
    
    // 500
    @ExceptionHandler(CustomInternalServerErrorException.class)
    public ResponseEntity<Map<String, Object>> handleCustomInternalServerErrorException(CustomInternalServerErrorException ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        return responseEntityBuilder(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), path,null);
    }

    // for @Valid annotations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        String path = request.getDescription(false).replace("uri=", "");
        
        return responseEntityBuilder(HttpStatus.BAD_REQUEST,"Validation failed",path, errors);
    }

    // for custom validations
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(ConstraintViolationException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        String path = request.getDescription(false).replace("uri=", "");
        
        return responseEntityBuilder(HttpStatus.BAD_REQUEST,"Validation failed",path, errors);
    }


    // validatoin of request parameters
    @SuppressWarnings("null")
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleMethodValidationExceptions(HandlerMethodValidationException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach(error -> {
            String[] s = error.getCodes()[0].trim().split("\\.");
            errors.put(s[s.length-1],error.getDefaultMessage());
        });
        String path = request.getDescription(false).replace("uri=", "");
        
        return responseEntityBuilder(HttpStatus.BAD_REQUEST, "Validation failed", path, errors);
    }


    // check for required parameters to not be null
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        String paramName = ex.getParameterName();
        errors.put(paramName, paramName + " parameter is missing");
        String path = request.getDescription(false).replace("uri=", "");
        
        return responseEntityBuilder(HttpStatus.BAD_REQUEST, "Missing request parameter", path, errors);
    }

}
