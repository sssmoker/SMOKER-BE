package com.ssmoker.smoker.global.exception;

import org.springframework.http.HttpStatus;

public class SmokerException extends RuntimeException {

    public final HttpStatus status;

    public SmokerException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
