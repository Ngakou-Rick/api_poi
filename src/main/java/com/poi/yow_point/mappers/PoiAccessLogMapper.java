package com.poi.yow_point.mappers;

import com.poi.yow_point.dto.PoiAccessLogDTO;
import com.poi.yow_point.models.PoiAccessLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class PoiAccessLogMapper {

    private final ObjectMapper objectMapper;

    public PoiAccessLogMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Mono<PoiAccessLogDTO> toDto(PoiAccessLog entity) {
        return Mono.fromCallable(() -> {
            PoiAccessLogDTO dto = PoiAccessLogDTO.builder()
                    .accessId(entity.getAccessId())
                    .poiId(entity.getPoiId())
                    .organizationId(entity.getOrganizationId())
                    .platformType(entity.getPlatformType())
                    .userId(entity.getUserId())
                    .accessType(entity.getAccessType())
                    .accessDatetime(entity.getAccessDatetime())
                    .metadata(parseMetadata(entity.getMetadata()))
                    .build();
            return dto;
        });
    }

    public Mono<PoiAccessLog> toEntity(PoiAccessLogDTO dto) {
        return Mono.fromCallable(() -> {
            PoiAccessLog entity = PoiAccessLog.builder()
                    .accessId(dto.getAccessId())
                    .poiId(dto.getPoiId())
                    .organizationId(dto.getOrganizationId())
                    .platformType(dto.getPlatformType())
                    .userId(dto.getUserId())
                    .accessType(dto.getAccessType())
                    .accessDatetime(dto.getAccessDatetime())
                    .metadata(formatMetadata(dto.getMetadata()))
                    .build();

            return entity;
        });
    }

    // Méthodes utilitaires pour la conversion des métadonnées JSON
    private Map<String, Object> parseMetadata(String metadataJson) {
        if (metadataJson == null || metadataJson.trim().isEmpty()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(metadataJson, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            // Log l'erreur si nécessaire
            return new HashMap<>();
        }
    }

    private String formatMetadata(Map<String, Object> metadata) {
        if (metadata == null || metadata.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            // Log l'erreur si nécessaire
            return null;
        }
    }

    // Méthodes de mapping pour des listes (utile pour les flux)
    public Mono<PoiAccessLogDTO> toDtoWithValidation(PoiAccessLog entity) {
        return toDto(entity)
                .doOnNext(dto -> {
                    // Validation supplémentaire si nécessaire
                    if (dto.getAccessDatetime() == null) {
                        throw new IllegalStateException("Access datetime is required");
                    }
                    if (dto.getPoiId() == null) {
                        throw new IllegalStateException("POI ID is required");
                    }
                });
    }

    public Mono<PoiAccessLog> toEntityWithDefaults(PoiAccessLogDTO dto) {
        return toEntity(dto)
                .map(entity -> {
                    // Application des valeurs par défaut si nécessaire
                    if (entity.getAccessDatetime() == null) {
                        entity.setAccessDatetime(java.time.OffsetDateTime.now());
                    }
                    if (entity.getAccessType() == null) {
                        entity.setAccessType("view"); // valeur par défaut
                    }
                    if (entity.getPlatformType() == null) {
                        entity.setPlatformType("Unknown");
                    }
                    return entity;
                });
    }

}