package com.whatting.domain.alert.presentation.dto.request;

import com.whatting.domain.alert.domain.AlertType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateAlertTypeRequest(
        @NotNull
        AlertType type,

        @NotBlank
        String message
) {
}
