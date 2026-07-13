package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.alert.domain.TeacherConfirmation;

import java.time.OffsetDateTime;

public record UpdateMyAlertStatusResponse(
        StudentStatus studentStatus,
        OffsetDateTime updatedAt,
        TeacherConfirmation teacherConfirmation
) {
}
