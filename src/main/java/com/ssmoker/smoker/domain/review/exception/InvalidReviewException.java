package com.ssmoker.smoker.domain.review.exception;

import com.ssmoker.smoker.global.exception.SmokerBadRequestException;
import com.ssmoker.smoker.global.exception.code.BaseErrorCode;

public class InvalidReviewException extends SmokerBadRequestException {
    public InvalidReviewException(BaseErrorCode code) {
        super(code);
    }
}
