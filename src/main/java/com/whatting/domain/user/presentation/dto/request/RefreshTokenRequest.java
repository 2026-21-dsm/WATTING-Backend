package com.whatting.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "refreshToken 값이 필수입니다")
        String refreshToken
) {
}
