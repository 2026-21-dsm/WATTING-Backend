package com.whatting.domain.alert.presentation.dto.response;

public record StudentStatusSummaryResponse(
        long helpRequestedCount,
        long noResponseCount,
        long evacuatingCount,
        long evacuatedCount
) {
}
