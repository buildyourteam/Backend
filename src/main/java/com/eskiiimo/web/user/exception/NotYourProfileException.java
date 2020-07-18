package com.eskiiimo.web.user.exception;

public class NotYourProfileException extends RuntimeException {
    public NotYourProfileException(String message) {
        super(message);
    }

    public NotYourProfileException(String message, Throwable cause) {
        super(message, cause);
    }
}
