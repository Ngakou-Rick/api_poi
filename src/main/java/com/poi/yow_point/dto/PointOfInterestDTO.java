package com.poi.yow_point.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.poi.yow_point.models.embeddable.AddressType;
import com.poi.yow_point.models.embeddable.ContactPersonType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointOfInterestDTO {
    private UUID poiId;
    private UUID orgId;
    private UUID townId;
    private UUID createdByUserId;
    private String poiName;
    private String poiType;
    private String poiCategory;
    private String poiLongName;
    private String poiShortName;
    private String poiFriendlyName;
    private String poiDescription;
    private String poiLogo; // Logo as Base64 String
    private List<String> poiImages;
    private Double latitude;
    private Double longitude;
    private AddressType poiAddress;
    private String phoneNumber;
    private String websiteUrl;
    private List<String> poiAmenities;
    private List<String> poiKeywords;
    private List<String> poiTypeTags;
    private String operationTimePlan; // JSON string
    private List<ContactPersonType> poiContacts;
    private Float popularityScore;
    private Boolean isActive;
    private String deactivationReason;
    private UUID deactivatedByUserId;
    private final OffsetDateTime createdAt = OffsetDateTime.now();
    private UUID updatedByUserId;
    private final OffsetDateTime updatedAt = OffsetDateTime.now();
}
