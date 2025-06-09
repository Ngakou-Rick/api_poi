package com.poi.yow_point.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate; // Pour WebSocket
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poi.yow_point.dto.NotificationPayload;
import com.poi.yow_point.enums.NotificationChannel;
import com.poi.yow_point.enums.NotificationType;
import com.poi.yow_point.models.Notification;
import com.poi.yow_point.models.User;
import com.poi.yow_point.repositories.NotificationRepository;
import com.poi.yow_point.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository; // Si persisté
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate; // Pour WebSocket

    // Méthode pour créer ET envoyer une notification
    @Transactional // Pour la persistance de la notification
    public void createAndSendNotification(String recipientUserId, NotificationType type, String title, String content,
                                          NotificationChannel channel, Map<String, Object> metadata) {
        User recipient = userRepository.findById(recipientUserId)
                .orElseThrow(() -> new RuntimeException("Recipient user not found: " + recipientUserId));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(type)
                .title(title)
                .content(content)
                .channel(channel) // Canal principal visé
                .metadata(metadata)
                .sent(false) // Marquer comme non envoyé initialement
                .build();

        Notification savedNotification = notificationRepository.save(notification); // Sauvegarder d'abord

        NotificationPayload payload = NotificationPayload.builder()
                .id(savedNotification.getNotificationId())
                .type(savedNotification.getType())
                .title(savedNotification.getTitle())
                .content(savedNotification.getContent())
                .metadata(savedNotification.getMetadata())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();

        boolean actuallySent = false;
        try {
            switch (channel) {
                case WEBSOCKET:
                    // Le 'username' pour sendToUser doit être le Principal.getName() de l'utilisateur cible
                    messagingTemplate.convertAndSendToUser(recipient.getUserId(), "/queue/notifications", payload);
                    log.info("Sent WebSocket notification to user {}: {}", recipientUserId, title);
                    actuallySent = true;
                    break;
                case EMAIL:
                    // TODO: Intégrer un service d'email (ex: Spring Mail)
                    log.info("TODO: Send Email notification to user {}: {}", recipientUserId, title);
                    // actuallySent = emailService.sendEmail(recipient.getEmail(), title, content);
                    break;
                case PUSH_MOBILE:
                    // TODO: Intégrer un service de push (ex: Firebase Cloud Messaging)
                    log.info("TODO: Send Push notification to user {}: {}", recipientUserId, title);
                    // actuallySent = pushService.sendPush(recipient.getPushToken(), title, content, metadata);
                    break;
                case SMS:
                     // TODO: Intégrer un service SMS
                    log.info("TODO: Send SMS notification to user {}: {}", recipientUserId, title);
                    break;
            }

            if (actuallySent) {
                savedNotification.setSent(true);
                savedNotification.setSentAt(LocalDateTime.now());
                notificationRepository.save(savedNotification); // Mettre à jour le statut d'envoi
            }

        } catch (Exception e) {
            log.error("Failed to send notification via {} to user {}: {}", channel, recipientUserId, e.getMessage(), e);
            // Gérer l'échec, potentiellement réessayer plus tard via un job
        }
    }

    @Transactional(readOnly = true)
    public Page<Notification> getNotificationsForUser(String userId, Pageable pageable) {
        return notificationRepository.findByRecipientUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional
    public void markNotificationAsRead(UUID notificationId, String userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!notification.getRecipient().getUserId().equals(userId)) {
            throw new RuntimeException("User not authorized to mark this notification as read");
        }
        if (notification.getReadAt() == null) {
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
        }
    }

     @Transactional(readOnly = true)
    public long getUnreadNotificationCount(String userId) {
        return notificationRepository.countByRecipientUserIdAndReadAtIsNull(userId);
    }
}

