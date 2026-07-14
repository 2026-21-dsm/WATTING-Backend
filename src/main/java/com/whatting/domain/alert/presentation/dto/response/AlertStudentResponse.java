package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.alert.domain.TeacherConfirmation;
import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.domain.user.domain.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AlertStudentResponse(
        UUID studentId,
        String name,
        Integer grade,
        Integer classNumber,
        Integer studentNumber,
        StudentStatus studentStatus,
        TeacherConfirmation teacherConfirmation,
        String confirmedByName,
        OffsetDateTime confirmedAt,
        HelpStatus helpStatus
) {
    public static AlertStudentResponse from(AlertParticipant participant, HelpStatus helpStatus) {
        User student = participant.getStudent();
        User confirmedBy = participant.getConfirmedBy();

        return new AlertStudentResponse(
                student.getUserId(),
                student.getName(),
                student.getGrade(),
                student.getClassNumber(),
                student.getStudentNumber(),
                participant.getStudentStatus(),
                participant.getTeacherConfirmation(),
                confirmedBy == null ? null : confirmedBy.getName(),
                participant.getConfirmedAt(),
                helpStatus
        );
    }
}
