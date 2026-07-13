package com.whatting.domain.help.presentation.dto.response;

import com.whatting.domain.help.domain.HelpRequest;
import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.domain.user.domain.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public record HelpRequestStatusResponse(
        UUID helpRequestId,
        HelpStatus status,
        HelpRequestHandledByResponse handledBy,
        OffsetDateTime acknowledgedAt,
        OffsetDateTime resolvedAt,
        String resolutionNote
) {
    public static HelpRequestStatusResponse from(HelpRequest helpRequest) {
        User handledBy = helpRequest.getHandledBy();
        HelpRequestHandledByResponse handledByResponse = handledBy == null
                ? null
                : new HelpRequestHandledByResponse(handledBy.getUserId(), handledBy.getName());

        return new HelpRequestStatusResponse(
                helpRequest.getHelpRequestId(),
                helpRequest.getStatus(),
                handledByResponse,
                helpRequest.getAcknowledgedAt(),
                helpRequest.getResolvedAt(),
                helpRequest.getResolutionNote()
        );
    }
}
