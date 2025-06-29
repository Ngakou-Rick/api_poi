package com.poi.yow_point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointOfInterestDTO {

    @JsonProperty("poi_id")
    private UUID poiId;

    @JsonProperty("created_by_user_id")
    private UUID createdByUserId;

    @JsonProperty("organization_id")
    private UUID organizationId;

    @JsonProperty("poi_name")
    private String poiName;

    @JsonProperty("poi_type")
    private String poiType;

    @JsonProperty("poi_category")
    private String poiCategory;

    @JsonProperty("poi_description")
    private String poiDescription;

    @JsonProperty("latitude")
    private BigDecimal latitude;

    @JsonProperty("longitude")
    private BigDecimal longitude;

    @JsonProperty("address_street_number")
    private String addressStreetNumber;

    @JsonProperty("address_street_name")
    private String addressStreetName;

    @JsonProperty("address_city")
    private String addressCity;

    @JsonProperty("address_postal_code")
    private String addressPostalCode;

    @JsonProperty("address_country")
    private String addressCountry;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("website_url")
    private String websiteUrl;

    @JsonProperty("operation_time_plan")
    private Map<String, Object> operationTimePlan;

    @JsonProperty("poi_contacts")
    private Map<String, Object> poiContacts;

    @JsonProperty("poi_images_urls")
    private List<String> poiImagesUrls;

    @JsonProperty("poi_amenities")
    private List<String> poiAmenities;

    @JsonProperty("poi_keywords")
    private List<String> poiKeywords;

    @JsonProperty("popularity_score")
    private Float popularityScore;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;
}