package com.eskiiimo.web.projects.exception;

public class ApplyNotFoundException extends RuntimeException {
    public ApplyNotFoundException(String userId) {
        super(userId + " 의 지원정보가 없습니다.");
    }
}
