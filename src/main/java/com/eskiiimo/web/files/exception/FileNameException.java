package com.eskiiimo.web.files.exception;

public class FileNameException extends RuntimeException {
    public FileNameException(String message) {
        super(message);
    }

    public FileNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
