package com.raf.pc.groupservice.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public CustomException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
