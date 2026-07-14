package com.whatting.domain.alert.presentation.dto.response;

public record TeacherConfirmationSummaryResponse(
        long confirmedCount,
        long unconfirmedCount
) {
}
