package com.whatting.domain.alert.repository;

import com.whatting.domain.alert.domain.Alert;
import com.whatting.domain.alert.domain.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    boolean existsByStatus(AlertStatus status);

    Optional<Alert> findByStatus(AlertStatus status);

    Optional<Alert> findByAlertId(UUID alertId);
}
