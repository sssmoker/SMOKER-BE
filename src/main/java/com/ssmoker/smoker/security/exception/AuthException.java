package com.ssmoker.smoker.security.exception;

import com.ssmoker.smoker.global.exception.GeneralException;
import com.ssmoker.smoker.global.exception.code.BaseErrorCode;

public class AuthException extends GeneralException {
    public AuthException(BaseErrorCode code) {
        super(code);
    }
}
