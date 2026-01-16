package com.poi.yow_point.infrastructure.adapters.inbound.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
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
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

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

    // Getters and Setters
    public UUID getPoiId() { return poiId; }
    public void setPoiId(UUID poiId) { this.poiId = poiId; }
    public UUID getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(UUID createdByUserId) { this.createdByUserId = createdByUserId; }
    public UUID getOrganizationId() { return organizationId; }
    public void setOrganizationId(UUID organizationId) { this.organizationId = organizationId; }
    public String getPoiName() { return poiName; }
    public void setPoiName(String poiName) { this.poiName = poiName; }
    public String getPoiType() { return poiType; }
    public void setPoiType(String poiType) { this.poiType = poiType; }
    public String getPoiCategory() { return poiCategory; }
    public void setPoiCategory(String poiCategory) { this.poiCategory = poiCategory; }
    public String getPoiDescription() { return poiDescription; }
    public void setPoiDescription(String poiDescription) { this.poiDescription = poiDescription; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getAddressStreetNumber() { return addressStreetNumber; }
    public void setAddressStreetNumber(String addressStreetNumber) { this.addressStreetNumber = addressStreetNumber; }
    public String getAddressStreetName() { return addressStreetName; }
    public void setAddressStreetName(String addressStreetName) { this.addressStreetName = addressStreetName; }
    public String getAddressCity() { return addressCity; }
    public void setAddressCity(String addressCity) { this.addressCity = addressCity; }
    public String getAddressPostalCode() { return addressPostalCode; }
    public void setAddressPostalCode(String addressPostalCode) { this.addressPostalCode = addressPostalCode; }
    public String getAddressCountry() { return addressCountry; }
    public void setAddressCountry(String addressCountry) { this.addressCountry = addressCountry; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
    public Map<String, Object> getOperationTimePlan() { return operationTimePlan; }
    public void setOperationTimePlan(Map<String, Object> operationTimePlan) { this.operationTimePlan = operationTimePlan; }
    public Map<String, Object> getPoiContacts() { return poiContacts; }
    public void setPoiContacts(Map<String, Object> poiContacts) { this.poiContacts = poiContacts; }
    public List<String> getPoiImagesUrls() { return poiImagesUrls; }
    public void setPoiImagesUrls(List<String> poiImagesUrls) { this.poiImagesUrls = poiImagesUrls; }
    public List<String> getPoiAmenities() { return poiAmenities; }
    public void setPoiAmenities(List<String> poiAmenities) { this.poiAmenities = poiAmenities; }
    public List<String> getPoiKeywords() { return poiKeywords; }
    public void setPoiKeywords(List<String> poiKeywords) { this.poiKeywords = poiKeywords; }
    public Float getPopularityScore() { return popularityScore; }
    public void setPopularityScore(Float popularityScore) { this.popularityScore = popularityScore; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}