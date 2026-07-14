package com.whatting.domain.alert.service;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.alert.domain.AlertStudentPriority;
import com.whatting.domain.alert.domain.AlertStatus;
import com.whatting.domain.help.domain.HelpStatus;
import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.alert.domain.TeacherConfirmation;
import com.whatting.domain.alert.exception.ActiveAlertExistsException;
import com.whatting.domain.alert.exception.AlertAlreadyClosedException;
import com.whatting.domain.alert.exception.AlertNotFoundException;
import com.whatting.domain.alert.exception.AlertNotClosedException;
import com.whatting.domain.alert.exception.AlertParticipantNotFoundException;
import com.whatting.domain.alert.exception.InvalidStudentStatusException;
import com.whatting.domain.alert.exception.StudentPermissionRequiredException;
import com.whatting.domain.alert.exception.TeacherPermissionRequiredException;
import com.whatting.domain.help.domain.HelpRequest;
import com.whatting.domain.help.exception.HelpRequestNotResolvedException;
import com.whatting.domain.alert.presentation.dto.request.CloseAlertRequest;
import com.whatting.domain.alert.presentation.dto.request.CreateAlertRequest;
import com.whatting.domain.alert.presentation.dto.request.UpdateMyAlertStatusRequest;
import com.whatting.domain.alert.presentation.dto.request.UpdateAlertTypeRequest;
import com.whatting.domain.alert.presentation.dto.response.ActiveAlertResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertCloseSummaryResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertDashboardResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertResultResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertResultSummaryResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertResultUnconfirmedStudentResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertResultUnresolvedHelpRequestResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertStudentListResponse;
import com.whatting.domain.alert.presentation.dto.response.AlertStudentResponse;
import com.whatting.domain.alert.presentation.dto.response.CloseAlertResponse;
import com.whatting.domain.alert.presentation.dto.response.CreateAlertResponse;
import com.whatting.domain.alert.presentation.dto.response.DashboardAlertResponse;
import com.whatting.domain.alert.presentation.dto.response.DashboardSummaryResponse;
import com.whatting.domain.alert.presentation.dto.response.HelpStatusSummaryResponse;
import com.whatting.domain.help.presentation.dto.response.HelpRequestResponse;
import com.whatting.domain.alert.presentation.dto.response.MyAlertStatusResponse;
import com.whatting.domain.alert.presentation.dto.response.StudentStatusSummaryResponse;
import com.whatting.domain.alert.presentation.dto.response.TeacherConfirmationSummaryResponse;
import com.whatting.domain.alert.presentation.dto.response.UpdateStudentConfirmationResponse;
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

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlertService {

    private static final ZoneId SERVICE_ZONE = ZoneId.of("Asia/Seoul");

    private static final List<HelpStatus> UNRESOLVED_HELP_STATUSES = List.of(
            HelpStatus.UNCHECKED,
            HelpStatus.ACKNOWLEDGED
    );

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
        long unresolvedHelpStudentCount = helpRequestRepository.countByAlertAndStatusIn(
                alert,
                UNRESOLVED_HELP_STATUSES
        );

        return new CloseAlertResponse(
                alert.getAlertId(),
                alert.getStatus(),
                alert.getReasonType(),
                alert.getCustomReason(),
                alert.getEndedAt(),
                new AlertCloseSummaryResponse(
                        participantCount,
                        confirmedCount,
                        unconfirmedCount,
                        unresolvedHelpStudentCount
                )
        );
    }

    @Transactional(readOnly = true)
    public AlertStudentListResponse getAlertStudents(UUID alertId, AlertStudentPriority priority, User requester) {
        requireTeacher(requester);
        Alert alert = getAlert(alertId);
        Map<Long, HelpStatus> helpStatusByParticipantId = helpRequestRepository
                .findByAlert(alert)
                .stream()
                .collect(Collectors.toMap(
                        helpRequest -> helpRequest.getParticipant().getId(),
                        helpRequest -> helpRequest.getStatus(),
                        (current, next) -> current
                ));

        List<AlertStudentResponse> items = alertParticipantRepository.findByAlert(alert).stream()
                .filter(participant -> matchesPriority(
                        participant,
                        priority,
                        helpStatusByParticipantId.get(participant.getId())
                ))
                .sorted(Comparator
                        .comparingInt((AlertParticipant participant) -> priorityOf(
                                participant,
                                helpStatusByParticipantId.get(participant.getId())
                        ))
                        .thenComparing(participant -> participant.getStudent().getGrade(), Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(participant -> participant.getStudent().getClassNumber(), Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(participant -> participant.getStudent().getStudentNumber(), Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(participant -> participant.getStudent().getName()))
                .map(participant -> AlertStudentResponse.from(
                        participant,
                        helpStatusByParticipantId.get(participant.getId())
                ))
                .toList();

        return new AlertStudentListResponse(items);
    }

    @Transactional(readOnly = true)
    public AlertDashboardResponse getAlertDashboard(UUID alertId, User requester) {
        requireTeacher(requester);
        Alert alert = getAlert(alertId);

        return new AlertDashboardResponse(
                DashboardAlertResponse.from(alert),
                createDashboardSummary(alert),
                OffsetDateTime.now(SERVICE_ZONE)
        );
    }

    @Transactional(readOnly = true)
    public AlertResultResponse getAlertResult(UUID alertId, User requester) {
        requireTeacher(requester);
        Alert alert = getAlert(alertId);
        if (!alert.isClosed()) {
            throw AlertNotClosedException.EXCEPTION;
        }

        List<AlertResultUnconfirmedStudentResponse> unconfirmedStudents = alertParticipantRepository
                .findByAlertAndTeacherConfirmation(alert, TeacherConfirmation.UNCONFIRMED)
                .stream()
                .sorted(Comparator
                        .comparing((AlertParticipant participant) -> participant.getStudent().getGrade(), Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(participant -> participant.getStudent().getClassNumber(), Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(participant -> participant.getStudent().getStudentNumber(), Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(participant -> participant.getStudent().getName()))
                .map(AlertResultUnconfirmedStudentResponse::from)
                .toList();
        List<AlertResultUnresolvedHelpRequestResponse> unresolvedHelpRequests = helpRequestRepository
                .findByAlertAndStatusIn(alert, UNRESOLVED_HELP_STATUSES)
                .stream()
                .sorted(Comparator
                        .comparingInt((HelpRequest helpRequest) -> priorityOf(helpRequest.getStatus()))
                        .thenComparing(HelpRequest::getCreatedAt))
                .map(AlertResultUnresolvedHelpRequestResponse::from)
                .toList();

        return new AlertResultResponse(
                alert.getAlertId(),
                alert.getType(),
                alert.getStatus(),
                alert.getStartedAt(),
                alert.getEndedAt(),
                alert.getReasonType(),
                alert.getCustomReason(),
                createResultSummary(alert),
                unconfirmedStudents,
                unresolvedHelpRequests
        );
    }

    @Transactional
    public UpdateStudentConfirmationResponse updateStudentConfirmation(
            UUID alertId,
            UUID studentId,
            User requester
    ) {
        User teacher = requireTeacher(requester);
        Alert alert = getAlert(alertId);
        if (alert.isClosed()) {
            throw AlertAlreadyClosedException.EXCEPTION;
        }

        AlertParticipant participant = alertParticipantRepository.findByAlertAndStudent_UserId(alert, studentId)
                .orElseThrow(() -> AlertParticipantNotFoundException.EXCEPTION);
        participant.toggleConfirmation(teacher);

        return UpdateStudentConfirmationResponse.from(participant);
    }

    @Transactional(readOnly = true)
    public MyAlertStatusResponse getMyAlertStatus(UUID alertId, User requester) {
        User student = requireStudent(requester);
        Alert alert = getAlert(alertId);
        AlertParticipant participant = getParticipant(alert, student);
        HelpRequestResponse helpRequest = helpRequestRepository
                .findByAlertAndParticipant(alert, participant)
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
        if (request.status() == StudentStatus.NO_RESPONSE || request.status() == StudentStatus.HELP_REQUESTED) {
            throw InvalidStudentStatusException.EXCEPTION;
        }

        AlertParticipant participant = getParticipant(alert, student);
        if (helpRequestRepository.existsByAlertAndParticipantAndStatusIn(alert, participant, UNRESOLVED_HELP_STATUSES)) {
            throw HelpRequestNotResolvedException.EXCEPTION;
        }
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

    private DashboardSummaryResponse createDashboardSummary(Alert alert) {
        long participantCount = alertParticipantRepository.countByAlert(alert);

        return new DashboardSummaryResponse(
                participantCount,
                createStudentStatusSummary(alert),
                createTeacherConfirmationSummary(alert, participantCount),
                createHelpStatusSummary(alert)
        );
    }

    private AlertResultSummaryResponse createResultSummary(Alert alert) {
        long participantCount = alertParticipantRepository.countByAlert(alert);
        long noResponseCount = alertParticipantRepository.countByAlertAndStudentStatus(
                alert,
                StudentStatus.NO_RESPONSE
        );

        return new AlertResultSummaryResponse(
                participantCount,
                participantCount - noResponseCount,
                createStudentStatusSummary(alert),
                createTeacherConfirmationSummary(alert, participantCount),
                createHelpStatusSummary(alert)
        );
    }

    private StudentStatusSummaryResponse createStudentStatusSummary(Alert alert) {
        return new StudentStatusSummaryResponse(
                alertParticipantRepository.countByAlertAndStudentStatus(alert, StudentStatus.HELP_REQUESTED),
                alertParticipantRepository.countByAlertAndStudentStatus(alert, StudentStatus.NO_RESPONSE),
                alertParticipantRepository.countByAlertAndStudentStatus(alert, StudentStatus.EVACUATING),
                alertParticipantRepository.countByAlertAndStudentStatus(alert, StudentStatus.EVACUATED)
        );
    }

    private TeacherConfirmationSummaryResponse createTeacherConfirmationSummary(Alert alert, long participantCount) {
        long confirmedCount = alertParticipantRepository.countByAlertAndTeacherConfirmation(
                alert,
                TeacherConfirmation.CONFIRMED
        );

        return new TeacherConfirmationSummaryResponse(
                confirmedCount,
                participantCount - confirmedCount
        );
    }

    private HelpStatusSummaryResponse createHelpStatusSummary(Alert alert) {
        return new HelpStatusSummaryResponse(
                helpRequestRepository.countByAlertAndStatus(alert, HelpStatus.UNCHECKED),
                helpRequestRepository.countByAlertAndStatus(alert, HelpStatus.ACKNOWLEDGED),
                helpRequestRepository.countByAlertAndStatus(alert, HelpStatus.RESOLVED)
        );
    }

    private boolean matchesPriority(
            AlertParticipant participant,
            AlertStudentPriority priority,
            HelpStatus helpStatus
    ) {
        if (priority == null) {
            return true;
        }

        return switch (priority) {
            case HELP -> participant.getStudentStatus() == StudentStatus.HELP_REQUESTED
                    && UNRESOLVED_HELP_STATUSES.contains(helpStatus);
            case NO_RESPONSE -> participant.getStudentStatus() == StudentStatus.NO_RESPONSE;
            case EVACUATING -> participant.getStudentStatus() == StudentStatus.EVACUATING;
            case EVACUATED -> participant.getStudentStatus() == StudentStatus.EVACUATED;
            case CONFIRMED -> participant.getTeacherConfirmation() == TeacherConfirmation.CONFIRMED;
        };
    }

    private int priorityOf(HelpStatus status) {
        return switch (status) {
            case UNCHECKED -> 0;
            case ACKNOWLEDGED -> 1;
            case RESOLVED -> 2;
        };
    }

    private int priorityOf(AlertParticipant participant, HelpStatus helpStatus) {
        if (participant.getStudentStatus() == StudentStatus.HELP_REQUESTED) {
            if (helpStatus == HelpStatus.UNCHECKED) {
                return 0;
            }
            if (helpStatus == HelpStatus.ACKNOWLEDGED) {
                return 1;
            }
            if (helpStatus == HelpStatus.RESOLVED) {
                return 5;
            }
            return 0;
        }

        return switch (participant.getStudentStatus()) {
            case NO_RESPONSE -> 2;
            case EVACUATING -> 3;
            case EVACUATED -> 4;
            case HELP_REQUESTED -> 5;
        };
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
