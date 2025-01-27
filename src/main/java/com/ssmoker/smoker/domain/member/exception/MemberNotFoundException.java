package com.ssmoker.smoker.domain.member.exception;

import com.ssmoker.smoker.global.exception.SmokerNotFoundException;
import com.ssmoker.smoker.global.exception.code.BaseErrorCode;

public class MemberNotFoundException extends SmokerNotFoundException {
    public MemberNotFoundException(BaseErrorCode code) {
        super(code);
    }
}
