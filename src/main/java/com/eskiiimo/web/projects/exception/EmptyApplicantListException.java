package com.eskiiimo.web.projects.exception;

import lombok.Getter;

@Getter
public class EmptyApplicantListException extends RuntimeException {

    private Long projectId;

    public EmptyApplicantListException(Long projectId) {
        this.projectId = projectId;
    }
}
