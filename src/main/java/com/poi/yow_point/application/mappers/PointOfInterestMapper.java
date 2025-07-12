package com.poi.yow_point.application.mappers;

import com.poi.yow_point.infrastructure.entities.PointOfInterest;
import com.poi.yow_point.presentation.dto.PointOfInterestDTO;

import io.r2dbc.postgresql.codec.Json;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
//import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PointOfInterestMapper {

    private final ObjectMapper objectMapper;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326); // SRID 4326 pour
                                                                                                     // WGS84

    /**
     * Convertit une entité PointOfInterest en DTO
     */
    public PointOfInterestDTO toDto(PointOfInterest entity) {
        if (entity == null) {
            return null;
        }

        return PointOfInterestDTO.builder()
                .poiId(entity.getPoiId())
                .createdByUserId(entity.getCreatedByUserId())
                .organizationId(entity.getOrganizationId())
                .poiName(entity.getPoiName())
                .poiType(entity.getPoiType())
                .poiCategory(entity.getPoiCategory())
                .poiDescription(entity.getPoiDescription())
                .latitude(entity.getLocation() != null ? entity.getLocation().getY() : null) // PostGIS: getY() =
                                                                                             // latitude
                .longitude(entity.getLocation() != null ? entity.getLocation().getX() : null) // PostGIS: getX() =
                                                                                              // longitude
                .addressStreetNumber(entity.getAddressStreetNumber())
                .addressStreetName(entity.getAddressStreetName())
                .addressCity(entity.getAddressCity())
                .addressPostalCode(entity.getAddressPostalCode())
                .addressCountry(entity.getAddressCountry())
                .phoneNumber(entity.getPhoneNumber())
                .websiteUrl(entity.getWebsiteUrl())
                .operationTimePlan(parseJsonToMap(entity.getOperationTimePlanJson()))
                .poiContacts(parseJsonToMap(entity.getPoiContactsJson()))
                .poiImagesUrls(convertStringToList(entity.getPoiImagesUrls()))
                .poiAmenities(convertStringToList(entity.getPoiAmenities()))
                .poiKeywords(convertStringToList(entity.getPoiKeywords()))
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

        // Crée un Point Geometry à partir des coordonnées
        Point location = null;
        if (dto.getLatitude() != null && dto.getLongitude() != null) {
            location = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
        }

        return PointOfInterest.builder()
                .poiId(dto.getPoiId())
                .createdByUserId(dto.getCreatedByUserId())
                .organizationId(dto.getOrganizationId())
                .poiName(dto.getPoiName())
                .poiType(dto.getPoiType())
                .poiCategory(dto.getPoiCategory())
                .poiDescription(dto.getPoiDescription())
                .location(location) // PostGIS: Stocke le Point
                .addressStreetNumber(dto.getAddressStreetNumber())
                .addressStreetName(dto.getAddressStreetName())
                .addressCity(dto.getAddressCity())
                .addressPostalCode(dto.getAddressPostalCode())
                .addressCountry(dto.getAddressCountry())
                .phoneNumber(dto.getPhoneNumber())
                .websiteUrl(dto.getWebsiteUrl())
                .operationTimePlanJson(convertMapToJson(dto.getOperationTimePlan()))
                .poiContactsJson(convertMapToJson(dto.getPoiContacts()))
                .poiImagesUrls(convertListToString(dto.getPoiImagesUrls()))
                .poiAmenities(convertListToString(dto.getPoiAmenities()))
                .poiKeywords(convertListToString(dto.getPoiKeywords()))
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

        // Mise à jour des champs de base
        if (dto.getPoiName() != null)
            existingEntity.setPoiName(dto.getPoiName());
        if (dto.getPoiType() != null)
            existingEntity.setPoiType(dto.getPoiType());
        if (dto.getPoiCategory() != null)
            existingEntity.setPoiCategory(dto.getPoiCategory());
        if (dto.getPoiDescription() != null)
            existingEntity.setPoiDescription(dto.getPoiDescription());

        // Mise à jour des coordonnées PostGIS
        if (dto.getLatitude() != null && dto.getLongitude() != null) {
            Point location = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
            existingEntity.setLocation(location);
        }

        // Mise à jour de l'adresse
        if (dto.getAddressStreetNumber() != null)
            existingEntity.setAddressStreetNumber(dto.getAddressStreetNumber());
        if (dto.getAddressStreetName() != null)
            existingEntity.setAddressStreetName(dto.getAddressStreetName());
        if (dto.getAddressCity() != null)
            existingEntity.setAddressCity(dto.getAddressCity());
        if (dto.getAddressPostalCode() != null)
            existingEntity.setAddressPostalCode(dto.getAddressPostalCode());
        if (dto.getAddressCountry() != null)
            existingEntity.setAddressCountry(dto.getAddressCountry());

        // Mise à jour des autres champs
        if (dto.getPhoneNumber() != null)
            existingEntity.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getWebsiteUrl() != null)
            existingEntity.setWebsiteUrl(dto.getWebsiteUrl());
        if (dto.getOperationTimePlan() != null)
            existingEntity.setOperationTimePlanJson(convertMapToJson(dto.getOperationTimePlan()));
        if (dto.getPoiContacts() != null)
            existingEntity.setPoiContactsJson(convertMapToJson(dto.getPoiContacts()));
        if (dto.getPoiImagesUrls() != null)
            existingEntity.setPoiImagesUrls(convertListToString(dto.getPoiImagesUrls()));
        if (dto.getPoiAmenities() != null)
            existingEntity.setPoiAmenities(convertListToString(dto.getPoiAmenities()));
        if (dto.getPoiKeywords() != null)
            existingEntity.setPoiKeywords(convertListToString(dto.getPoiKeywords()));
        if (dto.getPopularityScore() != null)
            existingEntity.setPopularityScore(dto.getPopularityScore());
        if (dto.getIsActive() != null)
            existingEntity.setIsActive(dto.getIsActive());

        existingEntity.setUpdatedAt(Instant.now()); // Toujours mettre à jour le timestamp

        return existingEntity;
    }

    // Méthodes utilitaires privées

    /**
     * Convertit un objet Json (R2DBC) en Map<String, Object>
     */
    private Map<String, Object> parseJsonToMap(Json jsonData) {
        if (jsonData == null) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(jsonData.asString(), new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * Convertit une Map en Json (R2DBC)
     */
    private Json convertMapToJson(Map<String, Object> map) {
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

    /**
     * Convertit une String CSV en List<String>
     */
    private List<String> convertStringToList(String csvString) {
        if (csvString == null || csvString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(csvString.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Convertit une List<String> en String CSV
     */
    private String convertListToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.trim().isEmpty())
                .collect(Collectors.joining(","));
    }
}