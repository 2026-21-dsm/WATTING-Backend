package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.AlertStatus;
import com.whatting.domain.alert.domain.AlertType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UpdateAlertTypeResponse(
        UUID alertId,
        AlertType type,
        AlertStatus status,
        String message,
        OffsetDateTime updatedAt
) {
}
