package com.whatting.domain.alert.presentation.dto.response;

import com.whatting.domain.user.domain.User;

import java.util.UUID;

public record AlertConfirmedByResponse(
        UUID userId,
        String name
) {
    public static AlertConfirmedByResponse from(User teacher) {
        if (teacher == null) {
            return null;
        }

        return new AlertConfirmedByResponse(
                teacher.getUserId(),
                teacher.getName()
        );
    }
}
