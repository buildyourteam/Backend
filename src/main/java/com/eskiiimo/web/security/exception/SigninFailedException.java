package com.eskiiimo.web.security.exception;

public class SigninFailedException extends RuntimeException {
    public SigninFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public SigninFailedException(String msg) {
        super(msg);
    }

    public SigninFailedException() {
        super("아이디 또는 비밀번호가 정확하지 않습니다.");
    }
}
