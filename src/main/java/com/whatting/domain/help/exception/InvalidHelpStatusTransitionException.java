package com.whatting.domain.help.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class InvalidHelpStatusTransitionException extends BusinessException {
    public static final InvalidHelpStatusTransitionException EXCEPTION = new InvalidHelpStatusTransitionException();

    private InvalidHelpStatusTransitionException() {
        super(ErrorCode.Invalid_Help_Status_Transition_Exception);
    }
}
