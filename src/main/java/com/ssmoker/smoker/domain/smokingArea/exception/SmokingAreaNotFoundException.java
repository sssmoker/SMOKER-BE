package com.ssmoker.smoker.domain.smokingArea.exception;

import com.ssmoker.smoker.global.exception.SmokerNotFoundException;
import com.ssmoker.smoker.global.exception.code.BaseErrorCode;

public class SmokingAreaNotFoundException extends SmokerNotFoundException {
    public SmokingAreaNotFoundException(BaseErrorCode code) {
        super(code);
    }
}
