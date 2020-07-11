package com.eskiiimo.web.projects.exception;

public class YouAreNotReaderException extends RuntimeException {
    public YouAreNotReaderException(String message) {
        super(message);
    }

    public YouAreNotReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
