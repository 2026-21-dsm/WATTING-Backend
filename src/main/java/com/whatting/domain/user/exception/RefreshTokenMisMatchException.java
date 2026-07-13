package com.whatting.domain.user.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class RefreshTokenMisMatchException extends BusinessException {
    public static final RefreshTokenMisMatchException EXCEPTION = new RefreshTokenMisMatchException();
    public RefreshTokenMisMatchException() {
        super(ErrorCode.Refresh_Token_Mis_Match_Exception);
    }
}
