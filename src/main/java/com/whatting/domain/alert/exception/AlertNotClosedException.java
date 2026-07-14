package com.whatting.domain.alert.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class AlertNotClosedException extends BusinessException {
    public static final AlertNotClosedException EXCEPTION = new AlertNotClosedException();

    private AlertNotClosedException() {
        super(ErrorCode.Alert_Not_Closed_Exception);
    }
}
