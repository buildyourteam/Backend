package com.eskiiimo.web.security.exception;

public class NotRegularUserException extends RuntimeException {
    public NotRegularUserException(String msg, Throwable t) {
        super(msg, t);
    }

    public NotRegularUserException(String msg) {
        super(msg);
    }

    public NotRegularUserException() {
        super("제제당하거나 탈퇴한 계정입니다.");
    }
}
