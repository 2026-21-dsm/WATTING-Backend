package com.whatting.domain.alert.presentation.dto.request;

import com.whatting.domain.alert.domain.AlertCloseReasonType;
import jakarta.validation.constraints.NotNull;

public record CloseAlertRequest(
        @NotNull
        AlertCloseReasonType reasonType,

        String customReason
) {
}
