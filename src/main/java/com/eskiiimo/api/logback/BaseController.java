package com.eskiiimo.api.logback;

import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity handleException(Throwable throwable) {
        logging(throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity handleNotFoundUser(UserNotFoundException userNotFoundException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userNotFoundException);
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
