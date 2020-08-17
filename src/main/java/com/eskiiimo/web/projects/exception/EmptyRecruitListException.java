package com.eskiiimo.web.projects.exception;

import lombok.Getter;

@Getter
public class EmptyRecruitListException extends RuntimeException{

    private String userId;

    public EmptyRecruitListException(String userId) {
        this.userId = userId;
    }
}
