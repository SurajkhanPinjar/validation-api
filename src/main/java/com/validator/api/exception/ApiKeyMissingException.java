package com.validator.api.exception;

public class ApiKeyMissingException extends RuntimeException {
    public ApiKeyMissingException(String msg) {
        super(msg);
    }
}