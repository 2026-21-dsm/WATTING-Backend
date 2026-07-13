package com.whatting.domain.user.presentation.dto.response;

import com.whatting.domain.user.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenWithRoleResponse {
    private final String accessToken;
    private final String refreshToken;
    private final Role role;
}
