package com.whatting.domain.user.presentation.dto.response;

import com.whatting.domain.user.domain.Role;
import com.whatting.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserResponse {
    private final UUID userId;
    private final Role role;
    private final String schoolName;
    private final Integer grade;
    private final Integer classNumber;
    private final Integer studentNumber;
    private final String name;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .role(user.getRole())
                .schoolName(user.getSchoolName())
                .grade(user.getGrade())
                .classNumber(user.getClassNumber())
                .studentNumber(user.getStudentNumber())
                .name(user.getName())
                .build();
    }
}
