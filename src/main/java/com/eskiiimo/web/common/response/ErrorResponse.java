package com.eskiiimo.web.common.response;


import lombok.Getter;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;
@Getter
public class ErrorResponse {

    private int error;
    private String message;

    public ErrorResponse(int error, String message){
        this.error  = error;
        this.message = message;
    }


}
