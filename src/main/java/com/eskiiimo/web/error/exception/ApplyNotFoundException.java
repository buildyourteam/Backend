package com.eskiiimo.web.error.exception;

public class ApplyNotFoundException extends RuntimeException {
    public ApplyNotFoundException(String message) {
        super(message);
    }

    public ApplyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
