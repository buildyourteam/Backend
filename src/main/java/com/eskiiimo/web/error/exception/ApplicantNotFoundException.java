package com.eskiiimo.web.error.exception;

public class ApplicantNotFoundException extends RuntimeException {
    public ApplicantNotFoundException(String message) {
        super(message);
    }

    public ApplicantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
