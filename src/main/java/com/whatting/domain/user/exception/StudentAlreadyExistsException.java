package com.whatting.domain.user.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class StudentAlreadyExistsException extends BusinessException {
    public static final StudentAlreadyExistsException EXCEPTION = new StudentAlreadyExistsException();

    public StudentAlreadyExistsException() {
        super(ErrorCode.Student_Already_Exists_Exception);
    }
}
