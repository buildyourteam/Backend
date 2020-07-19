package com.eskiiimo.web.user.exception;

public class YouAreNotMemberException extends RuntimeException {
    public YouAreNotMemberException(Long projectId) {
        super("projectID : '"+projectId+"' 프로젝트에 소속되어 있지 않습니다.");
    }
}
