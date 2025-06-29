package com.poi.yow_point.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poi.yow_point.dto.PoiAccessLogDTO;
import com.poi.yow_point.models.PoiAccessLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PoiAccessLogMapper {

    private final ObjectMapper objectMapper;

    /**
     * Convertit une entité PoiAccessLog en DTO
     */
    public Mono<PoiAccessLogDTO> toDTO(PoiAccessLog entity) {
        return Mono.fromCallable(() -> {
            if (entity == null) {
                return null;
            }

            PoiAccessLogDTO.PoiAccessLogDTOBuilder builder = PoiAccessLogDTO.builder()
                    .accessId(entity.getAccessId())
                    .poiId(entity.getPoiId())
                    .organizationId(entity.getOrganizationId())
                    .platformType(entity.getPlatformType())
                    .userId(entity.getUserId())
                    .accessType(entity.getAccessType())
                    .accessDatetime(entity.getAccessDatetime());

            // Conversion JsonNode vers Map<String, Object>
            if (entity.hasMetadata()) {
                try {
                    Map<String, Object> metadataMap = objectMapper.convertValue(
                            entity.getMetadata(),
                            new TypeReference<Map<String, Object>>() {
                            });
                    builder.metadata(metadataMap);
                } catch (Exception e) {
                    log.warn("Erreur lors de la conversion des métadonnées JsonNode vers Map pour l'entité {}: {}",
                            entity.getAccessId(), e.getMessage());
                    builder.metadata(null);
                }
            }

            return builder.build();
        });
    }

    /**
     * Convertit un DTO PoiAccessLogDTO en entité
     */
    public Mono<PoiAccessLog> toEntity(PoiAccessLogDTO dto) {
        return Mono.fromCallable(() -> {
            if (dto == null) {
                return null;
            }

            PoiAccessLog.PoiAccessLogBuilder builder = PoiAccessLog.builder()
                    .accessId(dto.getAccessId())
                    .poiId(dto.getPoiId())
                    .organizationId(dto.getOrganizationId())
                    .platformType(dto.getPlatformType())
                    .userId(dto.getUserId())
                    .accessType(dto.getAccessType())
                    .accessDatetime(dto.getAccessDatetime());

            // Conversion Map<String, Object> vers JsonNode
            if (dto.getMetadata() != null && !dto.getMetadata().isEmpty()) {
                try {
                    JsonNode metadataNode = objectMapper.valueToTree(dto.getMetadata());
                    builder.metadata(metadataNode);
                } catch (Exception e) {
                    log.warn("Erreur lors de la conversion des métadonnées Map vers JsonNode pour le DTO {}: {}",
                            dto.getAccessId(), e.getMessage());
                    builder.metadata(null);
                }
            }

            return builder.build();
        });
    }

    /**
     * Convertit une liste d'entités en liste de DTOs
     */
    public Flux<PoiAccessLogDTO> toDTOFlux(Flux<PoiAccessLog> entities) {
        return entities.flatMap(this::toDTO);
    }

    /**
     * Convertit une liste de DTOs en liste d'entités
     */
    public Flux<PoiAccessLog> toEntityFlux(Flux<PoiAccessLogDTO> dtos) {
        return dtos.flatMap(this::toEntity);
    }

    /**
     * Met à jour une entité existante avec les données d'un DTO
     * Utile pour les opérations de mise à jour partielle
     */
    public Mono<PoiAccessLog> updateEntityFromDTO(PoiAccessLog existingEntity, PoiAccessLogDTO dto) {
        return Mono.fromCallable(() -> {
            if (existingEntity == null || dto == null) {
                return existingEntity;
            }

            // Mise à jour des champs (en préservant l'ID et la date de création si
            // nécessaire)
            if (dto.getPoiId() != null) {
                existingEntity.setPoiId(dto.getPoiId());
            }
            if (dto.getOrganizationId() != null) {
                existingEntity.setOrganizationId(dto.getOrganizationId());
            }
            if (dto.getPlatformType() != null) {
                existingEntity.setPlatformType(dto.getPlatformType());
            }
            if (dto.getUserId() != null) {
                existingEntity.setUserId(dto.getUserId());
            }
            if (dto.getAccessType() != null) {
                existingEntity.setAccessType(dto.getAccessType());
            }
            if (dto.getAccessDatetime() != null) {
                existingEntity.setAccessDatetime(dto.getAccessDatetime());
            }

            // Mise à jour des métadonnées
            if (dto.getMetadata() != null) {
                try {
                    JsonNode metadataNode = objectMapper.valueToTree(dto.getMetadata());
                    existingEntity.setMetadata(metadataNode);
                } catch (Exception e) {
                    log.warn("Erreur lors de la mise à jour des métadonnées pour l'entité {}: {}",
                            existingEntity.getAccessId(), e.getMessage());
                }
            }

            return existingEntity;
        });
    }

    /**
     * Méthode utilitaire pour convertir JsonNode en Map de manière synchrone
     * À utiliser avec précaution dans un contexte réactif
     */
    public Map<String, Object> jsonNodeToMap(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull() || jsonNode.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            log.warn("Erreur lors de la conversion JsonNode vers Map: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Méthode utilitaire pour convertir Map en JsonNode de manière synchrone
     * À utiliser avec précaution dans un contexte réactif
     */
    public JsonNode mapToJsonNode(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.valueToTree(map);
        } catch (Exception e) {
            log.warn("Erreur lors de la conversion Map vers JsonNode: {}", e.getMessage());
            return null;
        }
    }
}