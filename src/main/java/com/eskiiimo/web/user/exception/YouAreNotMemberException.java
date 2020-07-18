package com.eskiiimo.web.user.exception;

public class YouAreNotMemberException extends RuntimeException {
    public YouAreNotMemberException(String message) {
        super(message);
    }

    public YouAreNotMemberException(String message, Throwable cause) {
        super(message, cause);
    }
}
