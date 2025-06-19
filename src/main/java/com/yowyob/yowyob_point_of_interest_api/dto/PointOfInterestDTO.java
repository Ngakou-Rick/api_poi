package com.yowyob.yowyob_point_of_interest_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

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
    // byte[] poiLogo; // Logo might be handled by a separate endpoint or as Base64 String
    private List<String> poiImages;
    private Double latitude;
    private Double longitude;
    private String locationGeogWKT; // For outputting WKT if needed
    private AddressTypeDTO poiAddress;
    private String phoneNumber;
    private String websiteUrl;
    private List<String> poiAmenities;
    private List<String> poiKeywords;
    private List<String> poiTypeTags;
    private String operationTimePlan; // JSON string
    private List<ContactPersonTypeDTO> poiContacts;
    private Float popularityScore;
    private Boolean isActive;
    private String deactivationReason;
    private UUID deactivatedByUserId;
    private OffsetDateTime createdAt;
    private UUID updatedByUserId;
    private OffsetDateTime updatedAt;
}
