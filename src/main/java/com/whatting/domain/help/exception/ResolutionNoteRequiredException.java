package com.whatting.domain.help.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class ResolutionNoteRequiredException extends BusinessException {
    public static final ResolutionNoteRequiredException EXCEPTION = new ResolutionNoteRequiredException();

    private ResolutionNoteRequiredException() {
        super(ErrorCode.Resolution_Note_Required_Exception);
    }
}
