package com.whatting.domain.alert.exception;

import com.whatting.global.error.exception.BusinessException;
import com.whatting.global.error.exception.ErrorCode;

public class AlertParticipantNotFoundException extends BusinessException {
    public static final AlertParticipantNotFoundException EXCEPTION = new AlertParticipantNotFoundException();

    private AlertParticipantNotFoundException() {
        super(ErrorCode.Alert_Participant_Not_Found_Exception);
    }
}
