package com.whatting.domain.alert.presentation.dto.response;

public record HelpStatusSummaryResponse(
        long uncheckedCount,
        long acknowledgedCount,
        long resolvedCount
) {
}
