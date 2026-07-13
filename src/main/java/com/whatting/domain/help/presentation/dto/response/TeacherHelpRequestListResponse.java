package com.whatting.domain.help.presentation.dto.response;

import java.util.List;

public record TeacherHelpRequestListResponse(
        List<TeacherHelpRequestResponse> items
) {
}
