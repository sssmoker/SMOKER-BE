package com.ssmoker.smoker.global.exception;

import com.ssmoker.smoker.global.exception.code.ErrorReasonDTO;
import com.ssmoker.smoker.global.exception.code.ErrorStatus;
import lombok.Getter;

@Getter
public class SmokerClientException extends SmokerException {

    private final ErrorReasonDTO reason;

    public SmokerClientException(final ErrorStatus errorStatus) {
        super(errorStatus.getHttpStatus(), errorStatus.getMessage());
        this.reason = errorStatus.getReason();
    }
}
