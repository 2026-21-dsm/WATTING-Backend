package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.help.domain.HelpCategory;
import com.whatting.domain.help.domain.HelpRequest;
import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.domain.user.domain.User;

import java.util.UUID;

public record AlertResultUnresolvedHelpRequestResponse(
        UUID helpRequestId,
        String studentName,
        Integer grade,
        Integer classNumber,
        Integer studentNumber,
        StudentStatus studentStatus,
        HelpStatus helpStatus,
        String locationText,
        HelpCategory category
) {
    public static AlertResultUnresolvedHelpRequestResponse from(HelpRequest helpRequest) {
        User student = helpRequest.getParticipant().getStudent();

        return new AlertResultUnresolvedHelpRequestResponse(
                helpRequest.getHelpRequestId(),
                student.getName(),
                student.getGrade(),
                student.getClassNumber(),
                student.getStudentNumber(),
                helpRequest.getParticipant().getStudentStatus(),
                helpRequest.getStatus(),
                helpRequest.getLocationText(),
                helpRequest.getCategory()
        );
    }
}
