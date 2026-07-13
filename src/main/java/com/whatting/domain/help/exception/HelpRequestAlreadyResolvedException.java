package com.whatting.domain.help.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class HelpRequestAlreadyResolvedException extends BusinessException {
    public static final HelpRequestAlreadyResolvedException EXCEPTION = new HelpRequestAlreadyResolvedException();

    private HelpRequestAlreadyResolvedException() {
        super(ErrorCode.Help_Request_Already_Resolved_Exception);
    }
}
