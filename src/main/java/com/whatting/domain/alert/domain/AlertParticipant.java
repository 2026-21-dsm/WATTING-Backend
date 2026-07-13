package com.whatting.domain.alert.domain;

import com.whatting.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Entity
@Table(
        name = "alert_participant_tbl",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_alert_participant",
                        columnNames = {"alert_id", "student_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlertParticipant {

    private static final ZoneId SERVICE_ZONE = ZoneId.of("Asia/Seoul");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id", nullable = false)
    private Alert alert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Enumerated(EnumType.STRING)
    @Column(name = "student_status", nullable = false, length = 30)
    private StudentStatus studentStatus;

    @Column(name = "student_status_updated_at")
    private OffsetDateTime studentStatusUpdatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "teacher_confirmation", nullable = false, length = 30)
    private TeacherConfirmation teacherConfirmation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmed_by")
    private User confirmedBy;

    @Column(name = "confirmed_at")
    private OffsetDateTime confirmedAt;

    public static AlertParticipant create(Alert alert, User student) {
        AlertParticipant participant = new AlertParticipant();
        participant.alert = alert;
        participant.student = student;
        participant.studentStatus = StudentStatus.NO_RESPONSE;
        participant.teacherConfirmation = TeacherConfirmation.UNCONFIRMED;
        return participant;
    }

    public void confirm(User teacher, boolean confirmed) {
        if (confirmed) {
            this.teacherConfirmation = TeacherConfirmation.CONFIRMED;
            this.confirmedBy = teacher;
            this.confirmedAt = OffsetDateTime.now(SERVICE_ZONE);
            return;
        }

        this.teacherConfirmation = TeacherConfirmation.UNCONFIRMED;
        this.confirmedBy = null;
        this.confirmedAt = null;
    }
}
