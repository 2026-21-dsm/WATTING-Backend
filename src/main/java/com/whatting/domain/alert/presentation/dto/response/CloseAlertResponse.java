package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.AlertCloseReasonType;
import com.whatting.domain.alert.domain.AlertStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CloseAlertResponse(
        UUID alertId,
        AlertStatus status,
        AlertCloseReasonType reasonType,
        String customReason,
        OffsetDateTime endedAt,
        AlertCloseSummaryResponse summary
) {
}
