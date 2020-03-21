package com.eskiiimo.api.error;

import com.eskiiimo.api.error.exception.*;
import com.eskiiimo.api.files.exception.FileDownloadException;
import com.eskiiimo.api.files.exception.FileNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandleController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandleController.class);

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity handleException(Throwable throwable) {
        logging(throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundProject(ProjectNotFoundException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND,exception);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundUser(UserNotFoundException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND,exception);
    }
    @ExceptionHandler(ApplyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundApply(ApplyNotFoundException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND,exception);
    }
    @ExceptionHandler(ApplicantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundApplicants(ApplicantNotFoundException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND,exception);
    }
    @ExceptionHandler(YouAreNotReaderException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleYouAreNotReader(YouAreNotReaderException exception){
        return new ErrorResponse(HttpStatus.FORBIDDEN,exception);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotSupprotException(HttpRequestMethodNotSupportedException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND,exception);
    }
    @ExceptionHandler(RecruitNotAuthException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleRecruitNotAuth(RecruitNotAuthException exception){
        return new ErrorResponse(HttpStatus.FORBIDDEN,exception);
    }
    @ExceptionHandler(RecruitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundRecruit(RecruitNotFoundException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND,exception);
    }
    @ExceptionHandler(FileNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleCantUploadFile(FileNameException exception){
        return new ErrorResponse(HttpStatus.BAD_REQUEST,exception);
    }
    @ExceptionHandler(FileDownloadException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundFile(FileDownloadException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND,exception);
    }

    protected void logging(Throwable throwable) {
        if (logger.isErrorEnabled()) {
            if (throwable.getMessage() != null) {
                logger.error(throwable.getMessage(), throwable);
            } else {
                logger.error("ERROR", throwable);
            }
        }
    }
}
