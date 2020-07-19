package com.eskiiimo.web.user.controller;

import com.eskiiimo.web.common.response.ErrorResponse;
import com.eskiiimo.web.user.exception.NotYourProfileException;
import com.eskiiimo.web.user.exception.YouAreNotMemberException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandleController {
    @ExceptionHandler(NotYourProfileException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleNotYourProfile(NotYourProfileException exception) {
        return new ErrorResponse(HttpStatus.FORBIDDEN,exception.getMessage());
    }

    @ExceptionHandler(YouAreNotMemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleYouAreNotMember(YouAreNotMemberException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }
}
