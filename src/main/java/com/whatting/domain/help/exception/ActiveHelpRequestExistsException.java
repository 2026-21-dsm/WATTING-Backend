package com.whatting.domain.help.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class ActiveHelpRequestExistsException extends BusinessException {
    public static final ActiveHelpRequestExistsException EXCEPTION = new ActiveHelpRequestExistsException();

    private ActiveHelpRequestExistsException() {
        super(ErrorCode.Active_Help_Request_Exists_Exception);
    }
}
