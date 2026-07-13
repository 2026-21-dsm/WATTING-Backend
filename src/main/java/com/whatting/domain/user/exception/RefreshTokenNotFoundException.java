package com.whatting.domain.user.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class RefreshTokenNotFoundException extends BusinessException {
    public static final RefreshTokenNotFoundException EXCEPTION = new RefreshTokenNotFoundException();
    public RefreshTokenNotFoundException() {
        super(ErrorCode.Refresh_Token_Not_Found_Exception);
    }
}
