package com.whatting.domain.user.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public static final UserNotFoundException EXCEPTION = new UserNotFoundException();
    public UserNotFoundException() {
        super(ErrorCode.User_Not_Found_Exception);
    }
}
