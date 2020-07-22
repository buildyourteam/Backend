package com.eskiiimo.web.projects.exception;

public class YouAreNotLeaderException extends RuntimeException {
    public YouAreNotLeaderException(String visitorId) {
        super("yourId : '" + visitorId + "'당신은 팀장이 아닙니다.");
    }
}
