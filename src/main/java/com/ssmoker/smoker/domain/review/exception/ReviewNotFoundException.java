package com.ssmoker.smoker.domain.review.exception;

import com.ssmoker.smoker.global.exception.SmokerNotFoundException;
import com.ssmoker.smoker.global.exception.code.BaseErrorCode;

public class ReviewNotFoundException extends SmokerNotFoundException {
    public ReviewNotFoundException(BaseErrorCode code) {
        super(code);
    }
}
