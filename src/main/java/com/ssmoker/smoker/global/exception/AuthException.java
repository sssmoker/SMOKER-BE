package com.ssmoker.smoker.global.exception;

import com.ssmoker.smoker.global.exception.code.BaseErrorCode;

public class AuthException extends GeneralException{
    public AuthException(BaseErrorCode code) {
        super(code);
    }
}
