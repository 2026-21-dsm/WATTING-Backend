package com.whatting.domain.user.presentation.dto.response;

import com.whatting.domain.user.domain.Role;
import com.whatting.domain.user.domain.User;

import java.util.UUID;

public record UserResponse(
        UUID userId,
        Role role,
        String schoolName,
        Integer grade,
        Integer classNumber,
        Integer studentNumber,
        String name
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getRole(),
                user.getSchoolName(),
                user.getGrade(),
                user.getClassNumber(),
                user.getStudentNumber(),
                user.getName()
        );
    }
}
