package com.whatting.domain.alert.service;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.alert.domain.AlertStatus;
import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.alert.domain.TeacherConfirmation;
import com.whatting.domain.alert.exception.ActiveAlertExistsException;
import com.whatting.domain.alert.exception.AlertAlreadyClosedException;
import com.whatting.domain.alert.exception.AlertNotFoundException;
import com.whatting.domain.alert.exception.AlertParticipantNotFoundException;
import com.whatting.domain.alert.exception.InvalidStudentStatusException;
import com.whatting.domain.alert.exception.StudentPermissionRequiredException;
import com.whatting.domain.alert.exception.TeacherPermissionRequiredException;
import com.whatting.domain.alert.presentation.dto.request.CloseAlertRequest;
import com.whatting.domain.alert.presentation.dto.request.CreateAlertRequest;
import com.whatting.domain.alert.presentation.dto.request.UpdateMyAlertStatusRequest;
import com.whatting.domain.alert.presentation.dto.request.UpdateAlertTypeRequest;
import com.whatting.domain.alert.presentation.dto.response.ActiveAlertResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertCloseSummaryResponse;
import com.whatting.domain.alert.presentation.dto.response.CloseAlertResponse;
import com.whatting.domain.alert.presentation.dto.response.CreateAlertResponse;
import com.whatting.domain.help.presentation.dto.response.HelpRequestResponse;
import com.whatting.domain.alert.presentation.dto.response.MyAlertStatusResponse;
import com.whatting.domain.alert.presentation.dto.response.UpdateAlertTypeResponse;
import com.whatting.domain.alert.presentation.dto.response.UpdateMyAlertStatusResponse;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertParticipantRepository alertParticipantRepository;
    private final HelpRequestRepository helpRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateAlertResponse createAlert(CreateAlertRequest request, User requester) {
        User teacher = requireTeacher(requester);

        if (alertRepository.existsByStatus(AlertStatus.ACTIVE)) {
            throw ActiveAlertExistsException.EXCEPTION;
        }

        Alert alert = alertRepository.save(Alert.create(
                request.type(),
                request.title(),
                request.message(),
                teacher
        ));

        List<User> students = userRepository.findAllByRole(Role.STUDENT);
        List<AlertParticipant> participants = students.stream()
                .map(student -> AlertParticipant.create(alert, student))
                .toList();
        alertParticipantRepository.saveAll(participants);

        return new CreateAlertResponse(
                alert.getAlertId(),
                alert.getType(),
                alert.getStatus(),
                alert.getTitle(),
                alert.getMessage(),
                participants.size(),
                alert.getStartedAt()
        );
    }

    @Transactional(readOnly = true)
    public Optional<ActiveAlertResponse> getActiveAlert(User requester) {
        requireAuthenticated(requester);

        return alertRepository.findByStatus(AlertStatus.ACTIVE)
                .map(alert -> new ActiveAlertResponse(
                        alert.getAlertId(),
                        alert.getType(),
                        alert.getStatus(),
                        alert.getTitle(),
                        alert.getMessage(),
                        alert.getStartedAt()
                ));
    }

    @Transactional
    public UpdateAlertTypeResponse updateAlertType(UUID alertId, UpdateAlertTypeRequest request, User requester) {
        requireTeacher(requester);

        Alert alert = getAlert(alertId);
        if (alert.isClosed()) {
            throw AlertAlreadyClosedException.EXCEPTION;
        }

        alert.changeType(request.type(), request.message());

        return new UpdateAlertTypeResponse(
                alert.getAlertId(),
                alert.getType(),
                alert.getStatus(),
                alert.getMessage(),
                alert.getUpdatedAt()
        );
    }

    @Transactional
    public CloseAlertResponse closeAlert(UUID alertId, CloseAlertRequest request, User requester) {
        User teacher = requireTeacher(requester);

        Alert alert = getAlert(alertId);
        if (alert.isClosed()) {
            throw AlertAlreadyClosedException.EXCEPTION;
        }

        alert.close(request.reasonType(), request.customReason(), teacher);

        long participantCount = alertParticipantRepository.countByAlert(alert);
        long confirmedCount = alertParticipantRepository.countByAlertAndTeacherConfirmation(
                alert,
                TeacherConfirmation.CONFIRMED
        );
        long unconfirmedCount = participantCount - confirmedCount;
        long unresolvedHelpCount = helpRequestRepository.countByAlertAndStatusIn(
                alert,
                List.of(HelpStatus.UNCHECKED, HelpStatus.ACKNOWLEDGED)
        );

        return new CloseAlertResponse(
                alert.getAlertId(),
                alert.getStatus(),
                alert.getReasonType(),
                alert.getEndedAt(),
                new AlertCloseSummaryResponse(
                        participantCount,
                        confirmedCount,
                        unconfirmedCount,
                        unresolvedHelpCount
                )
        );
    }

    @Transactional(readOnly = true)
    public MyAlertStatusResponse getMyAlertStatus(UUID alertId, User requester) {
        User student = requireStudent(requester);
        Alert alert = getAlert(alertId);
        AlertParticipant participant = getParticipant(alert, student);
        HelpRequestResponse helpRequest = helpRequestRepository
                .findFirstByAlertAndParticipantOrderByCreatedAtDesc(alert, participant)
                .map(HelpRequestResponse::from)
                .orElse(null);

        return new MyAlertStatusResponse(
                alert.getAlertId(),
                participant.getStudentStatus(),
                participant.getStudentStatusUpdatedAt(),
                participant.getTeacherConfirmation(),
                participant.getConfirmedAt(),
                helpRequest
        );
    }

    @Transactional
    public UpdateMyAlertStatusResponse updateMyAlertStatus(
            UUID alertId,
            UpdateMyAlertStatusRequest request,
            User requester
    ) {
        User student = requireStudent(requester);
        Alert alert = getAlert(alertId);
        if (alert.isClosed()) {
            throw AlertAlreadyClosedException.EXCEPTION;
        }
        if (request.status() == StudentStatus.NO_RESPONSE) {
            throw InvalidStudentStatusException.EXCEPTION;
        }

        AlertParticipant participant = getParticipant(alert, student);
        participant.updateStudentStatus(request.status());

        return new UpdateMyAlertStatusResponse(
                participant.getStudentStatus(),
                participant.getStudentStatusUpdatedAt(),
                participant.getTeacherConfirmation()
        );
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
