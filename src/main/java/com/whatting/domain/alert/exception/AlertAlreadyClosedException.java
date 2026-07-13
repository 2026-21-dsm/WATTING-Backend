package com.whatting.domain.alert.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class AlertAlreadyClosedException extends BusinessException {
    public static final AlertAlreadyClosedException EXCEPTION = new AlertAlreadyClosedException();

    private AlertAlreadyClosedException() {
        super(ErrorCode.Alert_Already_Closed_Exception);
    }
}
