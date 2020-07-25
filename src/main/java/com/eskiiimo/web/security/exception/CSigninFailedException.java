package com.eskiiimo.web.security.exception;

public class CSigninFailedException extends RuntimeException {
    public CSigninFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public CSigninFailedException(String msg) {
        super(msg);
    }

    public CSigninFailedException() {
        super("아이디 또는 비밀번호가 정확하지 않습니다.");
    }
}
