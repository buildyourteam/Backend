package com.eskiiimo.web.error.exception;

public class RecruitNotFoundException extends RuntimeException {
    public RecruitNotFoundException(String message) {
        super(message);
    }

    public RecruitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
