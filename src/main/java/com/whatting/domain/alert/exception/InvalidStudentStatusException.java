package com.whatting.domain.alert.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class InvalidStudentStatusException extends BusinessException {
    public static final InvalidStudentStatusException EXCEPTION = new InvalidStudentStatusException();

    private InvalidStudentStatusException() {
        super(ErrorCode.Invalid_Student_Status_Exception);
    }
}
