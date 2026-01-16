package com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity;

import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import io.r2dbc.postgresql.codec.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("point_of_interest")
public class PointOfInterestEntity {

    @Id
    @Column("poi_id")
    private UUID poiId;

    @Column("created_by_user_id")
    private UUID createdByUserId;

    @Column("organization_id")
    private UUID organizationId;

    @Column("poi_name")
    private String poiName;

    @Column("poi_type")
    private String poiType;

    @Column("poi_category")
    private String poiCategory;

    @Column("poi_description")
    private String poiDescription;

    // Utilisation de Point pour les coordonnées géographiques avec PostGIS
    @Column("location_geog")
    private Point location;

    @Column("address_street_number")
    private String addressStreetNumber;

    @Column("address_street_name")
    private String addressStreetName;

    @Column("address_city")
    private String addressCity;

    @Column("address_postal_code")
    private String addressPostalCode;

    @Column("address_country")
    private String addressCountry;

    @Column("phone_number")
    private String phoneNumber;

    @Column("website_url")
    private String websiteUrl;

    @Column("operation_time_plan")
    private Json operationTimePlanJson;

    @Column("poi_contacts")
    private Json poiContactsJson;

    @Column("poi_images_urls")
    private String poiImagesUrls;

    @Column("poi_amenities")
    private String poiAmenities;

    @Column("poi_keywords")
    private String poiKeywords;

    @Column("popularity_score")
    private Float popularityScore;

    @Column("is_active")
    private Boolean isActive;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
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
    public Point getLocation() { return location; }
    public void setLocation(Point location) { this.location = location; }
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
    public Json getOperationTimePlanJson() { return operationTimePlanJson; }
    public void setOperationTimePlanJson(Json operationTimePlanJson) { this.operationTimePlanJson = operationTimePlanJson; }
    public Json getPoiContactsJson() { return poiContactsJson; }
    public void setPoiContactsJson(Json poiContactsJson) { this.poiContactsJson = poiContactsJson; }
    public String getPoiImagesUrls() { return poiImagesUrls; }
    public void setPoiImagesUrls(String poiImagesUrls) { this.poiImagesUrls = poiImagesUrls; }
    public String getPoiAmenities() { return poiAmenities; }
    public void setPoiAmenities(String poiAmenities) { this.poiAmenities = poiAmenities; }
    public String getPoiKeywords() { return poiKeywords; }
    public void setPoiKeywords(String poiKeywords) { this.poiKeywords = poiKeywords; }
    public Float getPopularityScore() { return popularityScore; }
    public void setPopularityScore(Float popularityScore) { this.popularityScore = popularityScore; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    // Méthodes utilitaires
    public List<String> getPoiImagesUrlsList() {
        if (poiImagesUrls == null || poiImagesUrls.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(poiImagesUrls.split(","));
    }

    public void setPoiImagesUrlsList(List<String> urls) {
        this.poiImagesUrls = urls != null ? String.join(",", urls) : null;
    }

    public List<String> getPoiAmenitiesList() {
        if (poiAmenities == null || poiAmenities.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(poiAmenities.split(","));
    }

    public void setPoiAmenitiesList(List<String> amenities) {
        this.poiAmenities = amenities != null ? String.join(",", amenities) : null;
    }

    public List<String> getPoiKeywordsList() {
        if (poiImagesUrls == null || poiImagesUrls.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(poiImagesUrls.split(","));
    }

    public void setPoiKeywordsList(List<String> urls) {
        this.poiImagesUrls = urls != null ? String.join(",", urls) : null;
    }
}