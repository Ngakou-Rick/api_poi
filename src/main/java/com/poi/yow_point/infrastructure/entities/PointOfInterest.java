package com.poi.yow_point.infrastructure.entities;

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
public class PointOfInterest {

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