package com.whatting.domain.alert.presentation.dto.request;

import com.whatting.domain.alert.domain.AlertType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAlertRequest(
        @NotNull
        AlertType type,

        @NotBlank
        @Size(max = 100, message = "경보 제목은 최대 100자까지 입력할 수 있습니다")
        String title,

        @NotBlank
        @Size(max = 500, message = "경보 내용은 최대 500자까지 입력할 수 있습니다")
        String message
) {
}
