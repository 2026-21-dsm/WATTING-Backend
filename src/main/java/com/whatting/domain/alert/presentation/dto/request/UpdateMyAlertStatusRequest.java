package com.whatting.domain.alert.presentation.dto.request;

import com.whatting.domain.alert.domain.StudentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateMyAlertStatusRequest(
        @NotNull(message = "학생 상태는 필수입니다")
        StudentStatus status
) {
}
