package com.eskiiimo.web.user.controller;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.web.common.response.ErrorResponse;
import com.eskiiimo.web.user.exception.NotYourProfileException;
import com.eskiiimo.web.user.exception.UserNotFoundException;
import com.eskiiimo.web.user.exception.YouAreNotMemberException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User 서비스 예외처리기
 *
 * @author always0ne
 * @version 1.0
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandleController {
    /**
     * ERROR 201 당신의 프로필이 아닙니다.
     *
     * @param exception {@link NotYourProfileException}
     * @return {@link ProjectListDto} List
     */
    @ExceptionHandler(NotYourProfileException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleNotYourProfile(NotYourProfileException exception) {
        return new ErrorResponse("201", exception.getMessage());
    }

    /**
     * ERROR 202 존재하지 않는 사용자입니다.
     *
     * @param exception {@link UserNotFoundException}
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNotFoundUser(UserNotFoundException exception) {
        return new ErrorResponse("202", exception.getMessage());
    }

    /**
     * ERROR 203 프로젝트에 소속되지 않았습니다.
     *
     * @param exception {@link YouAreNotMemberException}
     * @return {@link ErrorResponse}
     */
    @ExceptionHandler(YouAreNotMemberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleYouAreNotMember(YouAreNotMemberException exception) {
        return new ErrorResponse("203", exception.getMessage());
    }
}
