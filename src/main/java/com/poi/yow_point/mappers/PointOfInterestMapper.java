package com.poi.yow_point.mappers;

import com.poi.yow_point.models.PointOfInterest;
import com.poi.yow_point.dto.PointOfInterestDTO;
import io.r2dbc.postgresql.codec.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PointOfInterestMapper {

    private final ObjectMapper objectMapper;

    public PointOfInterestMapper() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Convertit une entité PointOfInterest en DTO
     */
    public PointOfInterestDTO toDto(PointOfInterest entity) {
        if (entity == null) {
            return null;
        }

        return PointOfInterestDTO.builder()
                .poiId(entity.getPoiId())
                .createdByUserId(entity.getCreated_by_user_id())
                .organizationId(entity.getOrganizationId())
                .poiName(entity.getPoiName())
                .poiType(entity.getPoiType())
                .poiCategory(entity.getPoiCategory())
                .poiDescription(entity.getPoiDescription())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .addressStreetNumber(entity.getAddressStreetNumber())
                .addressStreetName(entity.getAddressStreetName())
                .addressCity(entity.getAddressCity())
                .addressPostalCode(entity.getAddressPostalCode())
                .addressCountry(entity.getAddressCountry())
                .phoneNumber(entity.getPhoneNumber())
                .websiteUrl(entity.getWebsiteUrl())
                .operationTimePlan(parseJsonToMap(entity.getOperationTimePlanJson()))
                .poiContacts(parseJsonToMap(entity.getPoiContactsJson()))
                .poiImagesUrls(entity.getPoiImagesUrlsList())
                .poiAmenities(entity.getPoiAmenitiesList())
                .poiKeywords(parseKeywords(entity.getPoiKeywords()))
                .popularityScore(entity.getPopularityScore())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Convertit un DTO en entité PointOfInterest
     */
    public PointOfInterest toEntity(PointOfInterestDTO dto) {
        if (dto == null) {
            return null;
        }

        return PointOfInterest.builder()
                .poiId(dto.getPoiId())
                .created_by_user_id(dto.getCreatedByUserId())
                .organizationId(dto.getOrganizationId())
                .poiName(dto.getPoiName())
                .poiType(dto.getPoiType())
                .poiCategory(dto.getPoiCategory())
                .poiDescription(dto.getPoiDescription())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .addressStreetNumber(dto.getAddressStreetNumber())
                .addressStreetName(dto.getAddressStreetName())
                .addressCity(dto.getAddressCity())
                .addressPostalCode(dto.getAddressPostalCode())
                .addressCountry(dto.getAddressCountry())
                .phoneNumber(dto.getPhoneNumber())
                .websiteUrl(dto.getWebsiteUrl())
                .operationTimePlanJson(mapToJson(dto.getOperationTimePlan()))
                .poiContactsJson(mapToJson(dto.getPoiContacts()))
                .poiImagesUrls(listToString(dto.getPoiImagesUrls()))
                .poiAmenities(listToString(dto.getPoiAmenities()))
                .poiKeywords(listToString(dto.getPoiKeywords()))
                .popularityScore(dto.getPopularityScore())
                .isActive(dto.getIsActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    /**
     * Met à jour une entité existante avec les données du DTO
     */
    public PointOfInterest updateEntityFromDto(PointOfInterest existingEntity, PointOfInterestDTO dto) {
        if (existingEntity == null || dto == null) {
            return existingEntity;
        }

        // Mise à jour des champs modifiables uniquement
        if (dto.getPoiName() != null) {
            existingEntity.setPoiName(dto.getPoiName());
        }
        if (dto.getPoiType() != null) {
            existingEntity.setPoiType(dto.getPoiType());
        }
        if (dto.getPoiCategory() != null) {
            existingEntity.setPoiCategory(dto.getPoiCategory());
        }
        if (dto.getPoiDescription() != null) {
            existingEntity.setPoiDescription(dto.getPoiDescription());
        }
        if (dto.getLatitude() != null) {
            existingEntity.setLatitude(dto.getLatitude());
        }
        if (dto.getLongitude() != null) {
            existingEntity.setLongitude(dto.getLongitude());
        }
        if (dto.getAddressStreetNumber() != null) {
            existingEntity.setAddressStreetNumber(dto.getAddressStreetNumber());
        }
        if (dto.getAddressStreetName() != null) {
            existingEntity.setAddressStreetName(dto.getAddressStreetName());
        }
        if (dto.getAddressCity() != null) {
            existingEntity.setAddressCity(dto.getAddressCity());
        }
        if (dto.getAddressPostalCode() != null) {
            existingEntity.setAddressPostalCode(dto.getAddressPostalCode());
        }
        if (dto.getAddressCountry() != null) {
            existingEntity.setAddressCountry(dto.getAddressCountry());
        }
        if (dto.getPhoneNumber() != null) {
            existingEntity.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getWebsiteUrl() != null) {
            existingEntity.setWebsiteUrl(dto.getWebsiteUrl());
        }
        if (dto.getOperationTimePlan() != null) {
            existingEntity.setOperationTimePlanJson(mapToJson(dto.getOperationTimePlan()));
        }
        if (dto.getPoiContacts() != null) {
            existingEntity.setPoiContactsJson(mapToJson(dto.getPoiContacts()));
        }
        if (dto.getPoiImagesUrls() != null) {
            existingEntity.setPoiImagesUrls(listToString(dto.getPoiImagesUrls()));
        }
        if (dto.getPoiAmenities() != null) {
            existingEntity.setPoiAmenities(listToString(dto.getPoiAmenities()));
        }
        if (dto.getPoiKeywords() != null) {
            existingEntity.setPoiKeywords(listToString(dto.getPoiKeywords()));
        }
        if (dto.getPopularityScore() != null) {
            existingEntity.setPopularityScore(dto.getPopularityScore());
        }
        if (dto.getIsActive() != null) {
            existingEntity.setIsActive(dto.getIsActive());
        }

        return existingEntity;
    }

    /**
     * Méthodes utilitaires privées
     */
    private Map<String, Object> parseJsonToMap(Json jsonData) {
        if (jsonData == null) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(jsonData.asString(),
                    new TypeReference<Map<String, Object>>() {
                    });
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private Json mapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return Json.of("{}");
        }
        try {
            String jsonString = objectMapper.writeValueAsString(map);
            return Json.of(jsonString);
        } catch (Exception e) {
            return Json.of("{}");
        }
    }

    private List<String> parseKeywords(String keywords) {
        if (keywords == null || keywords.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(keywords.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.joining(","));
    }
}