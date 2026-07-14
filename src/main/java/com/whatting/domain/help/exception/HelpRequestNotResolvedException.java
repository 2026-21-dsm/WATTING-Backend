package com.whatting.domain.help.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class HelpRequestNotResolvedException extends BusinessException {
    public static final HelpRequestNotResolvedException EXCEPTION = new HelpRequestNotResolvedException();

    private HelpRequestNotResolvedException() {
        super(ErrorCode.Help_Request_Not_Resolved_Exception);
    }
}
