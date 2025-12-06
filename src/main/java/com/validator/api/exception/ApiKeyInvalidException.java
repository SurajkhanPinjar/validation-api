package com.validator.api.exception;

public class ApiKeyInvalidException extends RuntimeException {
    public ApiKeyInvalidException(String msg) {
        super(msg);
    }
}