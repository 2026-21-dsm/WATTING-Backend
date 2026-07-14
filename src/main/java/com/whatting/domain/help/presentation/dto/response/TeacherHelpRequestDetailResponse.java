package com.whatting.domain.help.presentation.dto.response;

import com.whatting.domain.help.domain.HelpCategory;
import com.whatting.domain.help.domain.HelpRequest;
import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.domain.user.domain.User;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TeacherHelpRequestDetailResponse(
        UUID helpRequestId,
        HelpRequestStudentResponse student,
        HelpStatus status,
        String locationText,
        HelpCategory category,
        String details,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        HelpRequestHandledByResponse handledBy,
        OffsetDateTime acknowledgedAt,
        OffsetDateTime resolvedAt,
        String resolutionNote
) {
    public static TeacherHelpRequestDetailResponse from(HelpRequest helpRequest) {
        User handledBy = helpRequest.getHandledBy();
        HelpRequestHandledByResponse handledByResponse = handledBy == null
                ? null
                : new HelpRequestHandledByResponse(handledBy.getUserId(), handledBy.getName());

        return new TeacherHelpRequestDetailResponse(
                helpRequest.getHelpRequestId(),
                HelpRequestStudentResponse.from(helpRequest.getParticipant().getStudent()),
                helpRequest.getStatus(),
                helpRequest.getLocationText(),
                helpRequest.getCategory(),
                helpRequest.getDetails(),
                helpRequest.getCreatedAt(),
                helpRequest.getUpdatedAt(),
                handledByResponse,
                helpRequest.getAcknowledgedAt(),
                helpRequest.getResolvedAt(),
                helpRequest.getResolutionNote()
        );
    }
}
