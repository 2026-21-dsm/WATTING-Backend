package com.whatting.domain.alert.repository;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.alert.domain.TeacherConfirmation;
import com.whatting.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertParticipantRepository extends JpaRepository<AlertParticipant, Long> {

    long countByAlert(Alert alert);

    long countByAlertAndTeacherConfirmation(Alert alert, TeacherConfirmation teacherConfirmation);

    Optional<AlertParticipant> findByAlertAndStudent(Alert alert, User student);
}
