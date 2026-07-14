package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.user.domain.User;

import java.util.UUID;

public record AlertResultUnconfirmedStudentResponse(
        UUID studentId,
        String name,
        StudentStatus studentStatus
) {
    public static AlertResultUnconfirmedStudentResponse from(AlertParticipant participant) {
        User student = participant.getStudent();

        return new AlertResultUnconfirmedStudentResponse(
                student.getUserId(),
                student.getName(),
                participant.getStudentStatus()
        );
    }
}
