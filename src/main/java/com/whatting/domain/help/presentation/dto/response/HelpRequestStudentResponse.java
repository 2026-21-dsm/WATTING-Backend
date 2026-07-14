package com.whatting.domain.help.presentation.dto.response;

import com.whatting.domain.user.domain.User;

import java.util.UUID;

public record HelpRequestStudentResponse(
        UUID studentId,
        String name,
        Integer grade,
        Integer classNumber,
        Integer studentNumber
) {
    public static HelpRequestStudentResponse from(User student) {
        return new HelpRequestStudentResponse(
                student.getUserId(),
                student.getName(),
                student.getGrade(),
                student.getClassNumber(),
                student.getStudentNumber()
        );
    }
}
