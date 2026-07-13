package com.whatting.domain.user.presentation.dto.response;

import com.whatting.domain.user.domain.Role;

public record TokenWithRoleResponse(
        String accessToken,
        String refreshToken,
        Role role
) {
}
