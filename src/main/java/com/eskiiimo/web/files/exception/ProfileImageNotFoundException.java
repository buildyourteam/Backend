package com.eskiiimo.web.files.exception;

public class ProfileImageNotFoundException extends RuntimeException {
    public ProfileImageNotFoundException(String userId) {
        super("userId : '" + userId + "' 프로필 이미지가 없습니다.");
    }
}
