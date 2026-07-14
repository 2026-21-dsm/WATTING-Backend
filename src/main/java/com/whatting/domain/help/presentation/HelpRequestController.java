package com.whatting.domain.help.presentation;

import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.domain.help.exception.HelpRequestIdRequiredException;
import com.whatting.domain.help.presentation.dto.request.CreateHelpRequestRequest;
import com.whatting.domain.help.presentation.dto.request.UpdateHelpRequestStatusRequest;
import com.whatting.domain.help.presentation.dto.request.UpdateMyHelpRequestRequest;
import com.whatting.domain.help.presentation.dto.response.HelpRequestResponse;
import com.whatting.domain.help.presentation.dto.response.HelpRequestStatusResponse;
import com.whatting.domain.help.presentation.dto.response.TeacherHelpRequestDetailResponse;
import com.whatting.domain.help.presentation.dto.response.TeacherHelpRequestListResponse;
import com.whatting.domain.help.service.HelpRequestService;
import com.whatting.domain.user.domain.User;
import com.whatting.global.security.auth.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alerts/{alertId}/help-requests")
@RequiredArgsConstructor
public class HelpRequestController {

    private final HelpRequestService helpRequestService;

    @PostMapping
    public ResponseEntity<HelpRequestResponse> createHelpRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @Valid @RequestBody CreateHelpRequestRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(helpRequestService.createHelpRequest(alertId, request, getUser(userDetails)));
    }

    @GetMapping("/me")
    public ResponseEntity<HelpRequestResponse> getMyHelpRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId
    ) {
        return helpRequestService.getMyHelpRequest(alertId, getUser(userDetails))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PatchMapping("/me")
    public ResponseEntity<HelpRequestResponse> updateMyHelpRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @Valid @RequestBody UpdateMyHelpRequestRequest request
    ) {
        return ResponseEntity.ok(helpRequestService.updateMyHelpRequest(alertId, request, getUser(userDetails)));
    }

    @GetMapping
    public ResponseEntity<TeacherHelpRequestListResponse> getHelpRequests(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @RequestParam(name = "helpStatus", required = false) HelpStatus helpStatus
    ) {
        return ResponseEntity.ok(helpRequestService.getHelpRequests(alertId, helpStatus, getUser(userDetails)));
    }

    @GetMapping("/{helpRequestId}")
    public ResponseEntity<TeacherHelpRequestDetailResponse> getHelpRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @PathVariable UUID helpRequestId
    ) {
        return ResponseEntity.ok(helpRequestService.getHelpRequest(alertId, helpRequestId, getUser(userDetails)));
    }

    @PatchMapping("/{helpRequestId}/status")
    public ResponseEntity<HelpRequestStatusResponse> updateHelpRequestStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @PathVariable UUID helpRequestId,
            @Valid @RequestBody UpdateHelpRequestStatusRequest request
    ) {
        return ResponseEntity.ok(helpRequestService.updateHelpRequestStatus(
                alertId,
                helpRequestId,
                request,
                getUser(userDetails)
        ));
    }

    @PatchMapping("/status")
    public ResponseEntity<HelpRequestStatusResponse> updateHelpRequestStatusWithoutHelpRequestId() {
        throw HelpRequestIdRequiredException.EXCEPTION;
    }

    private User getUser(CustomUserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        return userDetails.getUser();
    }
}
