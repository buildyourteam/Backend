package com.eskiiimo.web.common.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Long projectId) {
        super("projectId : '"+projectId+"' 프로젝트가 존재하지 않습니다.");
    }
}
