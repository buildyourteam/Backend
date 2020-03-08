package com.eskiiimo.api.error.exception;

public class ApplyNotFoundException extends RuntimeException {
    public ApplyNotFoundException(String message) {
        super(message);
    }

    public ApplyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
