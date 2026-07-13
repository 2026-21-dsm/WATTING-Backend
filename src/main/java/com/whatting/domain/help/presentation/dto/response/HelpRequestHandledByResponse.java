package com.whatting.domain.help.presentation.dto.response;

import java.util.UUID;

public record HelpRequestHandledByResponse(
        UUID userId,
        String name
) {
}
