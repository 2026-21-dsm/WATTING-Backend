package com.whatting.domain.help.presentation.dto.request;

import com.whatting.domain.help.domain.HelpStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateHelpRequestStatusRequest(
        @NotNull(message = "도움 요청 상태는 필수입니다")
        HelpStatus helpStatus,

        @Size(max = 500, message = "처리 내용은 최대 500자까지 입력할 수 있습니다")
        String resolutionNote
) {
}
