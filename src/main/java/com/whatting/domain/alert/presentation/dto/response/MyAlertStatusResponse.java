package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.alert.domain.TeacherConfirmation;
import com.whatting.domain.help.presentation.dto.response.HelpRequestResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MyAlertStatusResponse(
        UUID alertId,
        StudentStatus studentStatus,
        OffsetDateTime studentStatusUpdatedAt,
        TeacherConfirmation teacherConfirmation,
        OffsetDateTime teacherConfirmedAt,
        HelpRequestResponse helpRequest
) {
}
