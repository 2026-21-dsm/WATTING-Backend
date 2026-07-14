package com.whatting.domain.alert.repository;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertParticipant;
import com.whatting.domain.alert.domain.StudentStatus;
import com.whatting.domain.alert.domain.TeacherConfirmation;
import com.whatting.domain.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlertParticipantRepository extends JpaRepository<AlertParticipant, Long> {

    long countByAlert(Alert alert);

    long countByAlertAndStudentStatus(Alert alert, StudentStatus studentStatus);

    long countByAlertAndTeacherConfirmation(Alert alert, TeacherConfirmation teacherConfirmation);

    Optional<AlertParticipant> findByAlertAndStudent(Alert alert, User student);

    @EntityGraph(attributePaths = {"student", "confirmedBy"})
    Optional<AlertParticipant> findByAlertAndStudent_UserId(Alert alert, UUID studentId);

    @EntityGraph(attributePaths = {"student", "confirmedBy"})
    List<AlertParticipant> findByAlert(Alert alert);

    @EntityGraph(attributePaths = {"student"})
    List<AlertParticipant> findByAlertAndTeacherConfirmation(Alert alert, TeacherConfirmation teacherConfirmation);
}
