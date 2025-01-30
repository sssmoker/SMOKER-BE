package com.ssmoker.smoker.domain.notice.exception;

import com.ssmoker.smoker.global.exception.GeneralException;
import com.ssmoker.smoker.global.exception.code.BaseErrorCode;

public class NoticeNotFoundException extends GeneralException {
    public NoticeNotFoundException(BaseErrorCode code) {
        super(code);
    }
}
