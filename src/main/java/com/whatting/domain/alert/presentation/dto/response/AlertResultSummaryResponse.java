package com.whatting.domain.alert.presentation.dto.response;

public record AlertResultSummaryResponse(
        long participantCount,
        long studentRespondedCount,
        StudentStatusSummaryResponse studentStatus,
        TeacherConfirmationSummaryResponse teacherConfirmation,
        HelpStatusSummaryResponse helpStatus
) {
}
