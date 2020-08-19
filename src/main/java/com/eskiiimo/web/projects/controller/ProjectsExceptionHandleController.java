package com.eskiiimo.web.projects.controller;

import com.eskiiimo.web.common.response.ErrorResponse;
import com.eskiiimo.web.projects.exception.*;
import com.eskiiimo.web.projects.response.GetApplicantsResponse;
import com.eskiiimo.web.projects.response.GetRecruitsResponse;
import com.eskiiimo.web.projects.response.GetRecruitsToMeResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ProjectsExceptionHandleController {

    @ExceptionHandler(ApplicantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundApplicants(ApplicantNotFoundException exception) {
        return new ErrorResponse("101", exception.getMessage());
    }

    @ExceptionHandler(ApplyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundApply(ApplyNotFoundException exception) {
        return new ErrorResponse("102", exception.getMessage());
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundProject(ProjectNotFoundException exception) {
        return new ErrorResponse("103", exception.getMessage());
    }

    @ExceptionHandler(RecruitNotAuthException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleRecruitNotAuth(RecruitNotAuthException exception) {
        return new ErrorResponse("104", exception.getMessage());
    }

    @ExceptionHandler(RecruitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundRecruit(RecruitNotFoundException exception) {
        return new ErrorResponse("105", exception.getMessage());
    }

    @ExceptionHandler(WrongDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleWrongDate(WrongDateException exception) {
        return new ErrorResponse("106", exception.getMessage());
    }

    @ExceptionHandler(YouAreNotLeaderException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleYouAreNotReader(YouAreNotLeaderException exception) {
        return new ErrorResponse("107", exception.getMessage());
    }

    @ExceptionHandler(DuplicateApplicantException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleDuplicateApplicant(DuplicateApplicantException exception) {
        return new ErrorResponse("108", exception.getMessage());
    }

    @ExceptionHandler(EmptyApplicantListException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GetApplicantsResponse handleEmptyListException(EmptyApplicantListException exception) {
        return new GetApplicantsResponse(exception.getProjectId());
    }

    @ExceptionHandler(EmptyRecruitListException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GetRecruitsToMeResponse handleEmptyRecruitListException(EmptyRecruitListException exception) {
        return new GetRecruitsToMeResponse(exception.getUserId());
    }
}
