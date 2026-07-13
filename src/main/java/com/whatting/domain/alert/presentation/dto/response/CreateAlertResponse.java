package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.AlertStatus;
import com.whatting.domain.alert.domain.AlertType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateAlertResponse(
        UUID alertId,
        AlertType type,
        AlertStatus status,
        String title,
        String message,
        long participantCount,
        OffsetDateTime startedAt
) {
}
