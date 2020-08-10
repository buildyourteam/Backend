package com.eskiiimo.web.projects.exception;

public class DuplicateApplicantException extends RuntimeException{

    public DuplicateApplicantException(String userId) {
        super(userId + "님은 이미 프로젝트에 지원하셨습니다.");
    }
}
