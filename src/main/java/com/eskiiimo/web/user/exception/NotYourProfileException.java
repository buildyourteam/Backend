package com.eskiiimo.web.user.exception;

public class NotYourProfileException extends RuntimeException {
    public NotYourProfileException(String userId) {
        super("userId : '"+userId+"' 프로필 수정 권한이 없습니다.");
    }
}
