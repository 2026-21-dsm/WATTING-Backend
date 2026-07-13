package com.whatting.domain.alert.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class StudentPermissionRequiredException extends BusinessException {
    public static final StudentPermissionRequiredException EXCEPTION = new StudentPermissionRequiredException();

    private StudentPermissionRequiredException() {
        super(ErrorCode.Student_Permission_Required_Exception);
    }
}
