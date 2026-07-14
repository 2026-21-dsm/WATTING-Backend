package com.whatting.domain.alert.presentation.dto.response;

public record AlertCloseSummaryResponse(
        long participantCount,
        long confirmedCount,
        long unconfirmedCount,
        long unresolvedHelpStudentCount
) {
}
