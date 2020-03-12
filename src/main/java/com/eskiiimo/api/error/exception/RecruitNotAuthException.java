package com.eskiiimo.api.error.exception;

public class RecruitNotAuthException extends RuntimeException {
    public RecruitNotAuthException(String message) {
        super(message);
    }

    public RecruitNotAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
