package com.whatting.domain.alert.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class AlertNotFoundException extends BusinessException {
    public static final AlertNotFoundException EXCEPTION = new AlertNotFoundException();

    private AlertNotFoundException() {
        super(ErrorCode.Alert_Not_Found_Exception);
    }
}
