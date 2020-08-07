package com.eskiiimo.web.security.controller;

import com.eskiiimo.web.common.response.ErrorResponse;
import com.eskiiimo.web.security.exception.CSigninFailedException;
import com.eskiiimo.web.security.exception.CUserNotFoundException;
import com.eskiiimo.web.security.exception.IdAlreadyExistsException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthExceptionHandleController {

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse userNotFound(CUserNotFoundException exception) {
        return new ErrorResponse(001, exception.getMessage());
    }

    @ExceptionHandler(CSigninFailedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorResponse signinFailed(CSigninFailedException exception) {
        return new ErrorResponse(002, exception.getMessage());
    }

    /**
     * 아이디 중복 예외 발생
     *
     * @param exception 아이디 중복 예외
     * @return ACCEPTED
     */
    @ExceptionHandler(IdAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleIdExists(IdAlreadyExistsException exception) {
        return new ErrorResponse(003, exception.getMessage());
    }

    /**
     * 서명이 유효하지 않은 예외 발생
     *
     * @param exception 서명이 서버와 다름
     * @return FORBIDDEN
     */
    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleSignature(SignatureException exception) {
        return new ErrorResponse(004, "유효하지 않은 토큰입니다.");
    }

    /**
     * 데이터가 깨진 토큰 예외 발생
     *
     * @param exception 토큰을 해석할 수 없음
     * @return FORBIDDEN
     */
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleMalformedJwt(MalformedJwtException exception) {
        return new ErrorResponse(005, "손상된 토큰입니다.");
    }

    /**
     * 토큰 만료 예외 발생
     *
     * @param exception 토큰 만료시간이 지남
     * @return FORBIDDEN
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleTokenExpired(ExpiredJwtException exception) {
        return new ErrorResponse(007, "만료된 토큰입니다.");
    }
}
