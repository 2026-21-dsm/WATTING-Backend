package com.whatting.domain.alert.presentation.dto.response;

public record DashboardSummaryResponse(
        long participantCount,
        StudentStatusSummaryResponse studentStatus,
        TeacherConfirmationSummaryResponse teacherConfirmation,
        HelpStatusSummaryResponse helpStatus
) {
}
