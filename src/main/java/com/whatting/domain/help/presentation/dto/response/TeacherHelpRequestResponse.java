package com.whatting.domain.help.presentation.dto.response;

import com.whatting.domain.help.domain.HelpCategory;
import com.whatting.domain.help.domain.HelpRequest;
import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.domain.user.domain.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TeacherHelpRequestResponse(
        UUID helpRequestId,
        String studentName,
        Integer grade,
        Integer classNumber,
        Integer studentNumber,
        HelpStatus status,
        String locationText,
        HelpCategory category,
        OffsetDateTime createdAt
) {
    public static TeacherHelpRequestResponse from(HelpRequest helpRequest) {
        User student = helpRequest.getParticipant().getStudent();

        return new TeacherHelpRequestResponse(
                helpRequest.getHelpRequestId(),
                student.getName(),
                student.getGrade(),
                student.getClassNumber(),
                student.getStudentNumber(),
                helpRequest.getStatus(),
                helpRequest.getLocationText(),
                helpRequest.getCategory(),
                helpRequest.getCreatedAt()
        );
    }
}
