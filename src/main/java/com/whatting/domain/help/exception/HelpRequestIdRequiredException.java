package com.whatting.domain.help.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class HelpRequestIdRequiredException extends BusinessException {
    public static final HelpRequestIdRequiredException EXCEPTION = new HelpRequestIdRequiredException();

    private HelpRequestIdRequiredException() {
        super(ErrorCode.Help_Request_Id_Required_Exception);
    }
}
