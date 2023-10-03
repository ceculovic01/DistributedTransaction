package com.raf.pc.studentservice.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends CustomException {

    public ConflictException(String message) {
        super(message, ErrorCode.CONFLICT, HttpStatus.CONFLICT);
    }
}
