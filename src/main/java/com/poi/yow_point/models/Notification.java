package com.poi.yow_point.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.poi.yow_point.enums.NotificationChannel;
import com.poi.yow_point.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_user_id", nullable = false) // L'utilisateur qui reçoit
    private AppUser recipient;

    @Enumerated(EnumType.STRING)
    @NotNull
    private NotificationType type;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String title; // Titre court de la notification

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String content; // Contenu détaillé

    @Enumerated(EnumType.STRING)
    @NotNull
    private NotificationChannel channel; // Canal par lequel elle a été (ou sera) envoyée

    @JdbcTypeCode(SqlTypes.JSON) // Nécessite hibernate-types
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata; // Données additionnelles (ex: poiId, reviewId, url)

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime readAt; // Marquer quand l'utilisateur l'a lue
    private boolean sent;         // Si elle a été effectivement envoyée (pour les systèmes de file d'attente)
    private LocalDateTime sentAt;
}