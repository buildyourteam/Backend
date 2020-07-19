package com.eskiiimo.web.projects.exception;

public class RecruitNotAuthException extends RuntimeException {
    public RecruitNotAuthException() {
        super("확인 권한이 없습니다.");
    }
}
