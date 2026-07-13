package com.whatting.domain.alert.presentation.dto.request;

import com.whatting.domain.alert.domain.AlertCloseReasonType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CloseAlertRequest(
        @NotNull
        AlertCloseReasonType reasonType,

        @Size(max = 500, message = "종료 사유는 최대 500자까지 입력할 수 있습니다")
        String customReason
) {
}
