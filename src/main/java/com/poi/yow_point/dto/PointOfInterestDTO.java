package com.poi.yow_point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.time.Instant;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointOfInterestDTO {

    private UUID poiId;
    private UUID organizationId;
    private String poiName;
    private String poiType;
    private String poiCategory;
    private String poiDescription;

    // Coordonnées GPS
    private BigDecimal latitude;
    private BigDecimal longitude;

    // Adresse décomposée
    private String addressStreetNumber;
    private String addressStreetName;
    private String addressCity;
    private String addressPostalCode;
    private String addressCountry;

    private String phoneNumber;
    private String websiteUrl;

    // Objets complexes (JSON)
    private Map<String, Object> operationTimePlan;
    private List<Map<String, Object>> poiContacts;

    // Listes
    private List<String> poiImagesUrls;
    private List<String> poiAmenities;
    private List<String> poiKeywords;

    private Float popularityScore;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}