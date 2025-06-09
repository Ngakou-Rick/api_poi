package com.poi.yow_point.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poi.yow_point.models.Notification;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByRecipientUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    long countByRecipientUserIdAndReadAtIsNull(String userId); // Pour le badge de notifications non lues
}

