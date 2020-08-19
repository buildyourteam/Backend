package com.eskiiimo.web.projects.exception;

public class DuplicatedRecruitException extends RuntimeException{

    public DuplicatedRecruitException(String userId) {
        super(userId + " 님에게 영입제안을 이미 보냈습니다. ");
    }
}