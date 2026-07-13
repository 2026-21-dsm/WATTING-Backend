package com.whatting.domain.alert.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class TeacherPermissionRequiredException extends BusinessException {
    public static final TeacherPermissionRequiredException EXCEPTION = new TeacherPermissionRequiredException();

    private TeacherPermissionRequiredException() {
        super(ErrorCode.Teacher_Permission_Required_Exception);
    }
}
