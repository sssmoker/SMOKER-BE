package com.ssmoker.smoker.domain.smokingArea.exception;

import com.ssmoker.smoker.global.exception.SmokerBadRequestException;
import com.ssmoker.smoker.global.exception.code.BaseErrorCode;

public class ReviewPageNumberException extends SmokerBadRequestException {
    public ReviewPageNumberException(BaseErrorCode code) {
        super(code);
    }
}
