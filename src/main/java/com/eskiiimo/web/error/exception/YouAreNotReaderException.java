package com.eskiiimo.web.error.exception;

public class YouAreNotReaderException extends RuntimeException {
    public YouAreNotReaderException(String message) {
        super(message);
    }

    public YouAreNotReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
