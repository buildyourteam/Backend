package com.eskiiimo.web.projects.exception;

public class WrongDateException extends RuntimeException {
    public WrongDateException(String message) {
        super("프로젝트 마감일이 잘못되었습니다. "+message);
    }

    public WrongDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
