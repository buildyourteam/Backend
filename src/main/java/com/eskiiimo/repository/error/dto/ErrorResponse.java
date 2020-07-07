package com.eskiiimo.repository.error.dto;


import lombok.Getter;
import org.springframework.http.HttpStatus;


import java.time.LocalDateTime;
@Getter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int httpStatusValue;
    private String httpStatus;
    private String errorMessage;

    public ErrorResponse(HttpStatus Httpstatus, Exception e){
        this.timestamp = LocalDateTime.now();
        this.httpStatusValue = Httpstatus.value();
        this.httpStatus = Httpstatus.toString();
        this.errorMessage = e.getMessage();
    }


}
