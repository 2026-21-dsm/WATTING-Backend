package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertStatus;
import com.whatting.domain.alert.domain.AlertType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DashboardAlertResponse(
        UUID alertId,
        AlertType type,
        AlertStatus status,
        OffsetDateTime startedAt
) {
    public static DashboardAlertResponse from(Alert alert) {
        return new DashboardAlertResponse(
                alert.getAlertId(),
                alert.getType(),
                alert.getStatus(),
                alert.getStartedAt()
        );
    }
}
