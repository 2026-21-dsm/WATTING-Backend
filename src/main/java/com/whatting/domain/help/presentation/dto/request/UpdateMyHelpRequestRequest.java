package com.whatting.domain.help.presentation.dto.request;

import com.whatting.domain.help.domain.HelpCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateMyHelpRequestRequest(
        @NotBlank(message = "위치 입력은 필수입니다")
        @Size(max = 200, message = "위치는 최대 200자까지 입력할 수 있습니다")
        String locationText,

        @NotNull(message = "도움 요청 유형은 필수입니다")
        HelpCategory category,

        @Size(max = 1000, message = "상세 내용은 최대 1000자까지 입력할 수 있습니다")
        String details
) {
}
