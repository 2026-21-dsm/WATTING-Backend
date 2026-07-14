package com.whatting.domain.help.service;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.help.domain.HelpRequest;
import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.domain.help.exception.ActiveHelpRequestExistsException;
import com.whatting.domain.alert.exception.AlertAlreadyClosedException;
import com.whatting.domain.alert.exception.AlertNotFoundException;
import com.whatting.domain.alert.exception.AlertParticipantNotFoundException;
import com.whatting.domain.help.exception.HelpRequestAlreadyResolvedException;
import com.whatting.domain.help.exception.HelpRequestNotFoundException;
import com.whatting.domain.help.exception.InvalidHelpStatusTransitionException;
import com.whatting.domain.help.exception.ResolutionNoteRequiredException;
import com.whatting.domain.alert.exception.StudentPermissionRequiredException;
import com.whatting.domain.alert.exception.TeacherPermissionRequiredException;
import com.whatting.domain.help.presentation.dto.request.CreateHelpRequestRequest;
import com.whatting.domain.help.presentation.dto.request.UpdateHelpRequestStatusRequest;
import com.whatting.domain.help.presentation.dto.request.UpdateMyHelpRequestRequest;
import com.whatting.domain.help.presentation.dto.response.HelpRequestResponse;
import com.whatting.domain.help.presentation.dto.response.HelpRequestStatusResponse;
import com.whatting.domain.help.presentation.dto.response.TeacherHelpRequestListResponse;
import com.whatting.domain.help.presentation.dto.response.TeacherHelpRequestDetailResponse;
import com.whatting.domain.help.presentation.dto.response.TeacherHelpRequestResponse;
import com.whatting.domain.alert.repository.AlertParticipantRepository;
import com.whatting.domain.alert.repository.AlertRepository;
import com.whatting.domain.help.repository.HelpRequestRepository;
import com.whatting.domain.user.domain.Role;
import com.whatting.domain.user.domain.User;
import com.whatting.domain.user.exception.UserNotFoundException;
import com.whatting.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HelpRequestService {

    private static final List<HelpStatus> UNRESOLVED_STATUSES = List.of(
            HelpStatus.UNCHECKED,
            HelpStatus.ACKNOWLEDGED
    );

    private final AlertRepository alertRepository;
    private final AlertParticipantRepository alertParticipantRepository;
    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public HelpRequestResponse createHelpRequest(UUID alertId, CreateHelpRequestRequest request, User requester) {
        User student = requireStudent(requester);
        Alert alert = getAlert(alertId);
        if (alert.isClosed()) {
            throw AlertAlreadyClosedException.EXCEPTION;
        }

        AlertParticipant participant = getParticipant(alert, student);
        if (helpRequestRepository.existsByAlertAndParticipantAndStatusIn(alert, participant, UNRESOLVED_STATUSES)) {
            throw ActiveHelpRequestExistsException.EXCEPTION;
        }

        HelpRequest helpRequest = HelpRequest.create(
                alert,
                participant,
                request.locationText(),
                request.category(),
                request.details()
        );
        participant.updateStudentStatus(StudentStatus.HELP_REQUESTED);

        return HelpRequestResponse.from(helpRequestRepository.save(helpRequest));
    }

    @Transactional(readOnly = true)
    public Optional<HelpRequestResponse> getMyHelpRequest(UUID alertId, User requester) {
        User student = requireStudent(requester);
        Alert alert = getAlert(alertId);
        AlertParticipant participant = getParticipant(alert, student);

        return helpRequestRepository.findFirstByAlertAndParticipantOrderByCreatedAtDesc(alert, participant)
                .map(HelpRequestResponse::from);
    }

    @Transactional
    public HelpRequestResponse updateMyHelpRequest(
            UUID alertId,
            UpdateMyHelpRequestRequest request,
            User requester
    ) {
        User student = requireStudent(requester);
        Alert alert = getAlert(alertId);
        if (alert.isClosed()) {
            throw AlertAlreadyClosedException.EXCEPTION;
        }

        AlertParticipant participant = getParticipant(alert, student);
        HelpRequest helpRequest = helpRequestRepository.findFirstByAlertAndParticipantOrderByCreatedAtDesc(
                alert,
                participant
        ).orElseThrow(() -> HelpRequestNotFoundException.EXCEPTION);
        if (helpRequest.isResolved()) {
            throw HelpRequestAlreadyResolvedException.EXCEPTION;
        }

        helpRequest.updateContent(request.locationText(), request.category(), request.details());

        return HelpRequestResponse.from(helpRequest);
    }

    @Transactional(readOnly = true)
    public TeacherHelpRequestListResponse getHelpRequests(UUID alertId, HelpStatus status, User requester) {
        requireTeacher(requester);
        Alert alert = getAlert(alertId);

        List<HelpRequest> helpRequests = status == null
                ? helpRequestRepository.findByAlert(alert)
                : helpRequestRepository.findByAlertAndStatus(alert, status);
        List<TeacherHelpRequestResponse> items = helpRequests.stream()
                .sorted(Comparator
                        .comparingInt((HelpRequest helpRequest) -> priorityOf(helpRequest.getStatus()))
                        .thenComparing(HelpRequest::getCreatedAt))
                .map(TeacherHelpRequestResponse::from)
                .toList();

        return new TeacherHelpRequestListResponse(items);
    }

    @Transactional(readOnly = true)
    public TeacherHelpRequestDetailResponse getHelpRequest(UUID alertId, UUID helpRequestId, User requester) {
        requireTeacher(requester);
        Alert alert = getAlert(alertId);
        HelpRequest helpRequest = helpRequestRepository.findByAlertAndHelpRequestId(alert, helpRequestId)
                .orElseThrow(() -> HelpRequestNotFoundException.EXCEPTION);

        return TeacherHelpRequestDetailResponse.from(helpRequest);
    }

    @Transactional
    public HelpRequestStatusResponse updateHelpRequestStatus(
            UUID alertId,
            UUID helpRequestId,
            UpdateHelpRequestStatusRequest request,
            User requester
    ) {
        User teacher = requireTeacher(requester);
        Alert alert = getAlert(alertId);
        if (alert.isClosed()) {
            throw AlertAlreadyClosedException.EXCEPTION;
        }

        HelpRequest helpRequest = helpRequestRepository.findByAlertAndHelpRequestId(alert, helpRequestId)
                .orElseThrow(() -> HelpRequestNotFoundException.EXCEPTION);
        changeStatus(helpRequest, request, teacher);

        return HelpRequestStatusResponse.from(helpRequest);
    }

    private void changeStatus(HelpRequest helpRequest, UpdateHelpRequestStatusRequest request, User teacher) {
        if (request.status() == HelpStatus.ACKNOWLEDGED && helpRequest.getStatus() == HelpStatus.UNCHECKED) {
            helpRequest.acknowledge(teacher);
            return;
        }

        if (request.status() == HelpStatus.RESOLVED && helpRequest.getStatus() == HelpStatus.ACKNOWLEDGED) {
            if (request.resolutionNote() == null || request.resolutionNote().isBlank()) {
                throw ResolutionNoteRequiredException.EXCEPTION;
            }
            helpRequest.resolve(request.resolutionNote());
            return;
        }

        throw InvalidHelpStatusTransitionException.EXCEPTION;
    }

    private int priorityOf(HelpStatus status) {
        return switch (status) {
            case UNCHECKED -> 0;
            case ACKNOWLEDGED -> 1;
            case RESOLVED -> 2;
        };
    }

    private Alert getAlert(UUID alertId) {
        return alertRepository.findByAlertId(alertId)
                .orElseThrow(() -> AlertNotFoundException.EXCEPTION);
    }

    private AlertParticipant getParticipant(Alert alert, User student) {
        return alertParticipantRepository.findByAlertAndStudent(alert, student)
                .orElseThrow(() -> AlertParticipantNotFoundException.EXCEPTION);
    }

    private User requireTeacher(User requester) {
        User user = requireAuthenticated(requester);
        if (user.getRole() != Role.TEACHER) {
            throw TeacherPermissionRequiredException.EXCEPTION;
        }
        return user;
    }

    private User requireStudent(User requester) {
        User user = requireAuthenticated(requester);
        if (user.getRole() != Role.STUDENT) {
            throw StudentPermissionRequiredException.EXCEPTION;
        }
        return user;
    }

    private User requireAuthenticated(User requester) {
        if (requester == null || requester.getId() == null) {
            throw UserNotFoundException.EXCEPTION;
        }

        return userRepository.findById(requester.getId())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }
}
