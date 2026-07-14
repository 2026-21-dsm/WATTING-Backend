package com.whatting.domain.help.repository;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.help.domain.HelpRequest;
import com.whatting.domain.help.domain.HelpStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {

    boolean existsByAlertAndParticipantAndStatusIn(
            Alert alert,
            AlertParticipant participant,
            Collection<HelpStatus> statuses
    );

    Optional<HelpRequest> findFirstByAlertAndParticipantOrderByCreatedAtDesc(
            Alert alert,
            AlertParticipant participant
    );

    @EntityGraph(attributePaths = {"participant", "participant.student", "handledBy"})
    Optional<HelpRequest> findByAlertAndHelpRequestId(Alert alert, UUID helpRequestId);

    @EntityGraph(attributePaths = {"participant", "participant.student"})
    List<HelpRequest> findByAlert(Alert alert);

    @EntityGraph(attributePaths = {"participant", "participant.student"})
    List<HelpRequest> findByAlertAndStatus(Alert alert, HelpStatus status);

    long countByAlertAndStatusIn(Alert alert, Collection<HelpStatus> statuses);
}
