package com.eskiiimo.api.error.exception;

public class ProjectMemberNotFoundException extends RuntimeException {
    public ProjectMemberNotFoundException(String message) {
        super(message);
    }

    public ProjectMemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
