package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.AlertCloseReasonType;
import com.whatting.domain.alert.domain.AlertStatus;
import com.whatting.domain.alert.domain.AlertType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record AlertResultResponse(
        UUID alertId,
        AlertType type,
        AlertStatus status,
        OffsetDateTime startedAt,
        OffsetDateTime endedAt,
        AlertCloseReasonType reasonType,
        String customReason,
        AlertResultSummaryResponse summary,
        List<AlertResultUnconfirmedStudentResponse> unconfirmedStudents
) {
}
