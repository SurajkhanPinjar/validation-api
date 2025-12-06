package com.validator.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("error", message);
        return body;
    }

    public static class InvalidEmailException extends RuntimeException {
        public InvalidEmailException(String message) { super(message); }
    }

    public static class InvalidPhoneException extends RuntimeException {
        public InvalidPhoneException(String message) { super(message); }
    }

    public static class InvalidZipException extends RuntimeException {
        public InvalidZipException(String message) { super(message); }
    }

    public static class InvalidIpException extends RuntimeException {
        public InvalidIpException(String message) { super(message); }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );
        body.put("success", false);
        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidEmail(InvalidEmailException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPhoneException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPhone(InvalidPhoneException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidZipException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidZip(InvalidZipException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidIpException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidIp(InvalidIpException ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(buildErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiKeyMissingException.class)
    public ResponseEntity<Map<String, Object>> handleMissingKey(ApiKeyMissingException ex) {
        return error("API_KEY_MISSING", ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ApiKeyInvalidException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidKey(ApiKeyInvalidException ex) {
        return error("API_KEY_INVALID", ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<Map<String, Object>> error(String code, String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("code", code);
        body.put("error", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(RateLimitExceededException ex) {
        return error("RATE_LIMIT_EXCEEDED", ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    public static class RateLimitExceededException extends RuntimeException {
        public RateLimitExceededException(String message) {
            super(message);
        }
    }
}
