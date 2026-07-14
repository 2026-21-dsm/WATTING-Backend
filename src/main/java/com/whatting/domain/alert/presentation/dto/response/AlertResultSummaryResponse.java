package com.whatting.domain.alert.presentation.dto.response;

public record AlertResultSummaryResponse(
        long participantCount,
        long studentRespondedCount,
        long helpRequestedCount,
        long confirmedCount,
        long unconfirmedCount,
        long helpRequestCount,
        long resolvedHelpCount
) {
}
