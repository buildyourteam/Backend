package com.eskiiimo.web.files.exception;

public class ProjectImageNotFoundException extends RuntimeException {
    public ProjectImageNotFoundException(Long projectId){
        super("userId : '" + projectId + "' 프로젝트 이미지가 없습니다.");
    }
}
