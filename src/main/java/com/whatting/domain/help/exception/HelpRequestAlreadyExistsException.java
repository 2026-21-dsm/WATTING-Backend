package com.whatting.domain.help.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class HelpRequestAlreadyExistsException extends BusinessException {
    public static final HelpRequestAlreadyExistsException EXCEPTION = new HelpRequestAlreadyExistsException();

    private HelpRequestAlreadyExistsException() {
        super(ErrorCode.Help_Request_Already_Exists_Exception);
    }
}
