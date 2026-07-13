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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Table(name = "alert_tbl")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alert {

    private static final ZoneId SERVICE_ZONE = ZoneId.of("Asia/Seoul");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "alert_id", nullable = false, unique = true, updatable = false)
    private UUID alertId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AlertStatus status;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_type", length = 50)
    private AlertCloseReasonType reasonType;

    @Column(name = "custom_reason", length = 500)
    private String customReason;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closed_by")
    private User closedBy;

    public static Alert create(AlertType type, String title, String message, User createdBy) {
        Alert alert = new Alert();
        alert.type = type;
        alert.status = AlertStatus.ACTIVE;
        alert.title = title;
        alert.message = message;
        alert.createdBy = createdBy;
        return alert;
    }

    public void changeType(AlertType type, String message) {
        this.type = type;
        this.message = message;
        this.updatedAt = now();
    }

    public void close(AlertCloseReasonType reasonType, String customReason, User closedBy) {
        this.status = AlertStatus.CLOSED;
        this.reasonType = reasonType;
        this.customReason = customReason;
        this.closedBy = closedBy;
        this.endedAt = now();
        this.updatedAt = this.endedAt;
    }

    public boolean isClosed() {
        return status == AlertStatus.CLOSED;
    }

    @PrePersist
    private void initialize() {
        if (alertId == null) {
            alertId = UUID.randomUUID();
        }
        if (startedAt == null) {
            startedAt = now();
        }
        if (updatedAt == null) {
            updatedAt = startedAt;
        }
    }

    private static OffsetDateTime now() {
        return OffsetDateTime.now(SERVICE_ZONE);
    }
}
