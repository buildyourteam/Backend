package com.eskiiimo.web.projects.exception;

public class YouAreNotReaderException extends RuntimeException {
    public YouAreNotReaderException(Long projectId) {
        super("projectId : '" + projectId + "'당신은 팀장이 아닙니다.");
    }
}
