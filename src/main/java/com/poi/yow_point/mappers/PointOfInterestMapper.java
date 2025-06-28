package com.poi.yow_point.mappers;

import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.models.PointOfInterest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class PointOfInterestMapper {

    private final ObjectMapper objectMapper;

    public PointOfInterestMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Mono<PointOfInterestDTO> toDto(PointOfInterest entity) {
        return Mono.fromCallable(() -> {
            PointOfInterestDTO dto = PointOfInterestDTO.builder()
                    .poiId(entity.getPoiId())
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
                    .poiImagesUrls(entity.getPoiImagesUrlsList())
                    .poiAmenities(entity.getPoiAmenitiesList())
                    .poiKeywords(parseKeywords(entity.getPoiKeywords()))
                    .popularityScore(entity.getPopularityScore())
                    .isActive(entity.getIsActive())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();

            // Conversion JSON pour les objets complexes
            dto.setOperationTimePlan(parseOperationTimePlan(entity.getOperationTimePlanJson()));
            dto.setPoiContacts(parsePoiContacts(entity.getPoiContactsJson()));

            return dto;
        });
    }

    public Mono<PointOfInterest> toEntity(PointOfInterestDTO dto) {
        return Mono.fromCallable(() -> {
            PointOfInterest entity = PointOfInterest.builder()
                    .poiId(dto.getPoiId())
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
                    .popularityScore(dto.getPopularityScore())
                    .isActive(dto.getIsActive())
                    .createdAt(dto.getCreatedAt())
                    .updatedAt(dto.getUpdatedAt())
                    .build();

            // Conversion des listes
            entity.setPoiImagesUrlsList(dto.getPoiImagesUrls());
            entity.setPoiAmenitiesList(dto.getPoiAmenities());
            entity.setPoiKeywords(formatKeywords(dto.getPoiKeywords()));

            // Conversion JSON pour les objets complexes
            entity.setOperationTimePlanJson(formatOperationTimePlan(dto.getOperationTimePlan()));
            entity.setPoiContactsJson(formatPoiContacts(dto.getPoiContacts()));

            return entity;
        });
    }

    // Méthodes utilitaires pour la conversion JSON
    private Map<String, Object> parseOperationTimePlan(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            return new HashMap<>();
        }
    }

    private String formatOperationTimePlan(Map<String, Object> operationTimePlan) {
        if (operationTimePlan == null || operationTimePlan.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(operationTimePlan);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private List<Map<String, Object>> parsePoiContacts(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    private String formatPoiContacts(List<Map<String, Object>> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(contacts);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private List<String> parseKeywords(String keywords) {
        if (keywords == null || keywords.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(keywords.split(","));
    }

    private String formatKeywords(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return null;
        }
        return String.join(",", keywords);
    }
}