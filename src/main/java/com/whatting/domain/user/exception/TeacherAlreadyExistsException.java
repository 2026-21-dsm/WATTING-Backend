package com.whatting.domain.user.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class TeacherAlreadyExistsException extends BusinessException {
    public static final TeacherAlreadyExistsException EXCEPTION = new TeacherAlreadyExistsException();

    public TeacherAlreadyExistsException() {
        super(ErrorCode.Teacher_Already_Exists_Exception);
    }
}
