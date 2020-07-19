package com.eskiiimo.web.projects.exception;

public class ApplicantNotFoundException extends RuntimeException {
    public ApplicantNotFoundException(Long projectId) {
        super("projectId : '"+projectId + "' 지원자가 없습니다.");
    }
}
