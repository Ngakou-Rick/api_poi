package com.poi.yow_point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.UUID;

import com.poi.yow_point.enums.NotificationType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPayload { // Ce qui est envoyé au client
    private UUID id; // ID de la notification si persistée
    private NotificationType type;
    private String title;
    private String content;
    private Map<String, Object> metadata; // Ex: {"poiId": "uuid-de-poi"}
    private String timestamp; // ISO 8601
}