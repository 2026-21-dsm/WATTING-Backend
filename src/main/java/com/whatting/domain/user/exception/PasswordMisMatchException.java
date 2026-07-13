package com.whatting.domain.user.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class PasswordMisMatchException extends BusinessException {
    public static final PasswordMisMatchException EXCEPTION = new PasswordMisMatchException();
    public PasswordMisMatchException() {
        super(ErrorCode.Password_Mis_Match_Exception);
    }
}