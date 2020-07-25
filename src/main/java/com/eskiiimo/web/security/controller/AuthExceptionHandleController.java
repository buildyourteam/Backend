package com.eskiiimo.web.security.controller;

import com.eskiiimo.web.common.response.ErrorResponse;
import com.eskiiimo.web.security.exception.CSigninFailedException;
import com.eskiiimo.web.security.exception.CUserNotFoundException;
import com.eskiiimo.web.user.exception.NotYourProfileException;
import com.eskiiimo.web.user.exception.UserNotFoundException;
import com.eskiiimo.web.user.exception.YouAreNotMemberException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthExceptionHandleController {
    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse userNotFound(CUserNotFoundException exception) {
        return new ErrorResponse(001, exception.getMessage());
    }

    @ExceptionHandler(CSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse signinFailed(CSigninFailedException exception) {
        return new ErrorResponse(002, exception.getMessage());
    }
}
