package com.poi.yow_point.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poi.yow_point.dto.NotificationPayload;
import com.poi.yow_point.models.Notification;
import com.poi.yow_point.services.NotificationService;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<Page<NotificationPayload>> getMyNotifications(Principal principal, Pageable pageable) {
        UUID userId = UUID.fromString(principal.getName());
        Page<Notification> notifications = notificationService.getNotificationsForUser(userId.toString(), pageable);
        // Mapper Notification vers NotificationPayload si nécessaire pour le client
        Page<NotificationPayload> payloadPage = notifications.map(n -> NotificationPayload.builder()
                .id(n.getNotificationId())
                .type(n.getType())
                .title(n.getTitle())
                .content(n.getContent())
                .metadata(n.getMetadata())
                .timestamp(n.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)) // Ou n.getSentAt()
                .build()
        );
        return ResponseEntity.ok(payloadPage);
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID notificationId, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        try {
            notificationService.markNotificationAsRead(notificationId, userId.toString());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build(); // Ou 404
        }
    }

    @GetMapping("/me/unread-count")
    public ResponseEntity<Long> getMyUnreadNotificationCount(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount(userId));
    }

    // Pas d'endpoint POST pour créer une notification directement par l'API par un utilisateur.
    // Les notifications sont généralement créées par des événements système.
}