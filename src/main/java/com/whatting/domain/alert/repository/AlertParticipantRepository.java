package com.whatting.domain.alert.repository;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.alert.domain.TeacherConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertParticipantRepository extends JpaRepository<AlertParticipant, Long> {

    long countByAlert(Alert alert);

    long countByAlertAndTeacherConfirmation(Alert alert, TeacherConfirmation teacherConfirmation);
}
