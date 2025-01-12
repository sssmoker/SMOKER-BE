package com.ssmoker.smoker.global.exception;

import com.ssmoker.smoker.global.exception.code.BaseErrorCode;
import com.ssmoker.smoker.global.exception.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmokerBadRequestException extends RuntimeException{

    private BaseErrorCode code;

    public SmokerBadRequestException(final String message) {
        super(message);
    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
