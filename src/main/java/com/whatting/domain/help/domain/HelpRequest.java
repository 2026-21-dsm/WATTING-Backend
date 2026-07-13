package com.whatting.domain.help.domain;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertParticipant;
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
@Table(name = "help_request_tbl")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelpRequest {

    private static final ZoneId SERVICE_ZONE = ZoneId.of("Asia/Seoul");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "help_request_id", nullable = false, unique = true, updatable = false)
    private UUID helpRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id", nullable = false)
    private Alert alert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private AlertParticipant participant;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private HelpStatus status;

    @Column(name = "location_text", nullable = false, length = 200)
    private String locationText;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private HelpCategory category;

    @Column(name = "details", length = 1000)
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handled_by")
    private User handledBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "acknowledged_at")
    private OffsetDateTime acknowledgedAt;

    @Column(name = "resolved_at")
    private OffsetDateTime resolvedAt;

    @Column(name = "resolution_note", length = 500)
    private String resolutionNote;

    public static HelpRequest create(
            Alert alert,
            AlertParticipant participant,
            String locationText,
            HelpCategory category,
            String details
    ) {
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.alert = alert;
        helpRequest.participant = participant;
        helpRequest.status = HelpStatus.UNCHECKED;
        helpRequest.locationText = locationText;
        helpRequest.category = category;
        helpRequest.details = details;
        return helpRequest;
    }

    public void updateContent(String locationText, HelpCategory category, String details) {
        this.locationText = locationText;
        this.category = category;
        this.details = details;
        this.updatedAt = now();
    }

    public void acknowledge(User teacher) {
        this.status = HelpStatus.ACKNOWLEDGED;
        this.handledBy = teacher;
        this.acknowledgedAt = now();
        this.updatedAt = this.acknowledgedAt;
    }

    public void resolve(String resolutionNote) {
        this.status = HelpStatus.RESOLVED;
        this.resolutionNote = resolutionNote;
        this.resolvedAt = now();
        this.updatedAt = this.resolvedAt;
    }

    public boolean isResolved() {
        return status == HelpStatus.RESOLVED;
    }

    @PrePersist
    private void initialize() {
        if (helpRequestId == null) {
            helpRequestId = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }

    private static OffsetDateTime now() {
        return OffsetDateTime.now(SERVICE_ZONE);
    }
}
