package com.whatting.domain.user.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class InvalidTeacherCodeException extends BusinessException {
    public static final InvalidTeacherCodeException EXCEPTION = new InvalidTeacherCodeException();

    public InvalidTeacherCodeException() {
        super(ErrorCode.Invalid_Teacher_Code_Exception);
    }
}
