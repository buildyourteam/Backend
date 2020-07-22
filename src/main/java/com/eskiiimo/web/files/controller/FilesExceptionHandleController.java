package com.eskiiimo.web.files.controller;

import com.eskiiimo.web.common.response.ErrorResponse;
import com.eskiiimo.web.files.exception.FileDownloadException;
import com.eskiiimo.web.files.exception.FileNameException;
import com.eskiiimo.web.files.exception.ProfileImageNotFoundException;
import com.eskiiimo.web.files.exception.ProjectImageNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FilesExceptionHandleController {

    @ExceptionHandler(FileDownloadException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundFile(FileDownloadException exception) {
        return new ErrorResponse(302, exception.getMessage());
    }

    @ExceptionHandler(FileNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleCantUploadFile(FileNameException exception) {
        return new ErrorResponse(303, exception.getMessage());
    }

    @ExceptionHandler(ProfileImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleProfileImageNotFound(ProfileImageNotFoundException exception) {
        return new ErrorResponse(305, exception.getMessage());
    }

    @ExceptionHandler(ProjectImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleProjectImageNotFound(ProjectImageNotFoundException exception) {
        return new ErrorResponse(306, exception.getMessage());
    }
}
