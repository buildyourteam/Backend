package com.eskiiimo.web.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("userId : '"+ userId + "' 사용자가 존재하지 않습니다.");
    }
}
