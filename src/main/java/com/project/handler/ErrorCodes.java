package com.project.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCodes {

    NO_CODE(NOT_IMPLEMENTED , "NO Error Code"),
    INCORRECT_PASSWORD( BAD_REQUEST , "Password Incorrect"),
    ACCOUNT_DISABLED( FORBIDDEN , "User Account Is Disabled"),
    ACCOUNT_LOCKED( FORBIDDEN , "User Account Is Locked"),
    BAD_CREDENTIALS( BAD_REQUEST , "Username or Password Incorrect"),
    ;


    @Getter
    private final int code ;
    @Getter
    private final HttpStatus httpStatus ;
    @Getter
    private final String description ;

    ErrorCodes( HttpStatus httpStatus, String description) {
        this.code = httpStatus.value();
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
