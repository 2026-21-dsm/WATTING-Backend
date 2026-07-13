package com.whatting.domain.alert.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class ActiveAlertExistsException extends BusinessException {
    public static final ActiveAlertExistsException EXCEPTION = new ActiveAlertExistsException();

    private ActiveAlertExistsException() {
        super(ErrorCode.Active_Alert_Exists_Exception);
    }
}
