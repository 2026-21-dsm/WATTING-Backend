package com.whatting.domain.alert.presentation.dto.response;

import java.util.List;

public record AlertStudentListResponse(
        List<AlertStudentResponse> items
) {
}
