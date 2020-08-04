package com.eskiiimo.web.user.exception;

public class NotYourProfileException extends RuntimeException {
    public NotYourProfileException(String userId) {
        super("userId : '" + userId + "' 당신의 프로필이 아닙니다.");
    }
}
