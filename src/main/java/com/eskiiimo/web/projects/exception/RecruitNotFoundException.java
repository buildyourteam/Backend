package com.eskiiimo.web.projects.exception;

public class RecruitNotFoundException extends RuntimeException {
    public RecruitNotFoundException() {
        super("영입을 제안한 사람이 없습니다.");
    }
}
