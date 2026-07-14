package com.whatting.domain.alert.presentation;

import com.whatting.domain.alert.domain.AlertStudentPriority;
import com.whatting.domain.alert.presentation.dto.request.CloseAlertRequest;
import com.whatting.domain.alert.presentation.dto.request.CreateAlertRequest;
import com.whatting.domain.alert.presentation.dto.request.UpdateMyAlertStatusRequest;
import com.whatting.domain.alert.presentation.dto.request.UpdateAlertTypeRequest;
import com.whatting.domain.alert.presentation.dto.response.ActiveAlertResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertDashboardResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertResultResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertStudentListResponse;
import com.whatting.domain.alert.presentation.dto.response.CloseAlertResponse;
import com.whatting.domain.alert.presentation.dto.response.CreateAlertResponse;
import com.whatting.domain.alert.presentation.dto.response.MyAlertStatusResponse;
import com.whatting.domain.alert.presentation.dto.response.UpdateStudentConfirmationResponse;
import com.whatting.domain.alert.presentation.dto.response.UpdateAlertTypeResponse;
import com.whatting.domain.alert.presentation.dto.response.UpdateMyAlertStatusResponse;
import com.whatting.domain.alert.service.AlertService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<CreateAlertResponse> createAlert(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateAlertRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alertService.createAlert(request, getUser(userDetails)));
    }

    @GetMapping("/active")
    public ResponseEntity<ActiveAlertResponse> getActiveAlert(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return alertService.getActiveAlert(getUser(userDetails))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PatchMapping("/{alertId}/type")
    public ResponseEntity<UpdateAlertTypeResponse> updateAlertType(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @Valid @RequestBody UpdateAlertTypeRequest request
    ) {
        return ResponseEntity.ok(alertService.updateAlertType(alertId, request, getUser(userDetails)));
    }

    @PostMapping("/{alertId}/close")
    public ResponseEntity<CloseAlertResponse> closeAlert(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @Valid @RequestBody CloseAlertRequest request
    ) {
        return ResponseEntity.ok(alertService.closeAlert(alertId, request, getUser(userDetails)));
    }

    @GetMapping("/{alertId}/students")
    public ResponseEntity<AlertStudentListResponse> getAlertStudents(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @RequestParam(required = false) AlertStudentPriority priority
    ) {
        return ResponseEntity.ok(alertService.getAlertStudents(alertId, priority, getUser(userDetails)));
    }

    @GetMapping("/{alertId}/dashboard")
    public ResponseEntity<AlertDashboardResponse> getAlertDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId
    ) {
        return ResponseEntity.ok(alertService.getAlertDashboard(alertId, getUser(userDetails)));
    }

    @GetMapping("/{alertId}/result")
    public ResponseEntity<AlertResultResponse> getAlertResult(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId
    ) {
        return ResponseEntity.ok(alertService.getAlertResult(alertId, getUser(userDetails)));
    }

    @PatchMapping("/{alertId}/students/{studentId}/confirmation")
    public ResponseEntity<UpdateStudentConfirmationResponse> updateStudentConfirmation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @PathVariable UUID studentId
    ) {
        return ResponseEntity.ok(alertService.updateStudentConfirmation(
                alertId,
                studentId,
                getUser(userDetails)
        ));
    }

    @GetMapping("/{alertId}/me")
    public ResponseEntity<MyAlertStatusResponse> getMyAlertStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId
    ) {
        return ResponseEntity.ok(alertService.getMyAlertStatus(alertId, getUser(userDetails)));
    }

    @PutMapping("/{alertId}/me/status")
    public ResponseEntity<UpdateMyAlertStatusResponse> updateMyAlertStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID alertId,
            @Valid @RequestBody UpdateMyAlertStatusRequest request
    ) {
        return ResponseEntity.ok(alertService.updateMyAlertStatus(alertId, request, getUser(userDetails)));
    }

    private User getUser(CustomUserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        return userDetails.getUser();
    }
}
