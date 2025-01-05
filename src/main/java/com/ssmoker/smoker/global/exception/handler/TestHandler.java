package com.ssmoker.smoker.global.exception.handler;

import com.ssmoker.smoker.global.exception.GeneralException;
import com.ssmoker.smoker.global.exception.code.BaseErrorCode;

// Test 를 위한 커스텀 예외
public class TestHandler extends GeneralException {

    public TestHandler(BaseErrorCode code) {
        super(code);
    }
}
