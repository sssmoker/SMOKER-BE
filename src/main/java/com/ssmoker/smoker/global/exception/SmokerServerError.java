package com.ssmoker.smoker.global.exception;

import org.springframework.http.HttpStatus;

public class SmokerServerError extends SmokerException {

    public SmokerServerError(final String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
