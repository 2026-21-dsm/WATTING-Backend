package com.whatting.domain.alert.presentation.dto.response;

import java.time.OffsetDateTime;

public record AlertDashboardResponse(
        DashboardAlertResponse alert,
        DashboardSummaryResponse summary,
        OffsetDateTime lastUpdatedAt
) {
}
