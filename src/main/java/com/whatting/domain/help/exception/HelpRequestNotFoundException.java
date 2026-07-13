package com.whatting.domain.help.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class HelpRequestNotFoundException extends BusinessException {
    public static final HelpRequestNotFoundException EXCEPTION = new HelpRequestNotFoundException();

    private HelpRequestNotFoundException() {
        super(ErrorCode.Help_Request_Not_Found_Exception);
    }
}
