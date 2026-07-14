package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.alert.domain.TeacherConfirmation;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UpdateStudentConfirmationResponse(
        UUID studentId,
        StudentStatus studentStatus,
        TeacherConfirmation teacherConfirmation,
        AlertConfirmedByResponse confirmedBy,
        OffsetDateTime confirmedAt
) {
    public static UpdateStudentConfirmationResponse from(AlertParticipant participant) {
        return new UpdateStudentConfirmationResponse(
                participant.getStudent().getUserId(),
                participant.getStudentStatus(),
                participant.getTeacherConfirmation(),
                AlertConfirmedByResponse.from(participant.getConfirmedBy()),
                participant.getConfirmedAt()
        );
    }
}
