package com.whatting.domain.help.presentation.dto.response;

import com.whatting.domain.help.domain.HelpCategory;
import com.whatting.domain.help.domain.HelpRequest;
import com.whatting.domain.help.domain.HelpStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record HelpRequestResponse(
        UUID helpRequestId,
        HelpStatus helpStatus,
        String locationText,
        HelpCategory category,
        String details,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime acknowledgedAt,
        OffsetDateTime resolvedAt,
        String resolutionNote
) {
    public static HelpRequestResponse from(HelpRequest helpRequest) {
        return new HelpRequestResponse(
                helpRequest.getHelpRequestId(),
                helpRequest.getStatus(),
                helpRequest.getLocationText(),
                helpRequest.getCategory(),
                helpRequest.getDetails(),
                helpRequest.getCreatedAt(),
                helpRequest.getUpdatedAt(),
                helpRequest.getAcknowledgedAt(),
                helpRequest.getResolvedAt(),
                helpRequest.getResolutionNote()
        );
    }
}
