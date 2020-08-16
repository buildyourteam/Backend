package com.eskiiimo.web.common.response;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int error;
    private String message;

    public ErrorResponse() {

    }

    public ErrorResponse(int error, String message) {
        this.error = error;
        this.message = message;
    }


}
