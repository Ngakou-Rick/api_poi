package com.yowyob.yowyob_point_of_interest_api.model;

// Remove com.yowyob.yowyob_point_of_interest_api.model.utils.AddressType;
// Remove com.yowyob.yowyob_point_of_interest_api.model.utils.ContactPersonType;
// Remove org.locationtech.jts.geom.Point;
// Remove org.hibernate.annotations.JdbcTypeCode;
// Remove org.hibernate.type.SqlTypes;
// Remove org.hibernate.annotations.UpdateTimestamp;
// Remove jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("point_of_interest")
public class PointOfInterest {

    @Id
    @Column("poi_id")
    private UUID poiId; // Must be set before save

    @Column("org_id")
    private UUID orgId;

    @Column("town_id")
    private UUID townId;

    @Column("created_by_user_id")
    private UUID createdByUserId;

    @Column("poi_name")
    private String poiName;

    @Column("poi_type")
    private String poiType;

    @Column("poi_category")
    private String poiCategory;

    @Column("poi_long_name")
    private String poiLongName;

    @Column("poi_short_name")
    private String poiShortName;

    @Column("poi_friendly_name")
    private String poiFriendlyName;

    @Column("poi_description") // TEXT in DB, LOB was for JPA with potential for CLOB
    private String poiDescription;

    @Column("poi_logo") // BYTEA
    private byte[] poiLogo;

    @Column("poi_images") // TEXT[]
    private List<String> poiImages;

    @Column("location_geog") // Storing as WKT string, actual column type is geometry(Point,4326)
    private String locationGeogWkt;

    // Flattened AddressType fields
    @Column("street_number")
    private String poiAddressStreetNumber;
    @Column("street_name")
    private String poiAddressStreetName;
    @Column("city")
    private String poiAddressCity;
    @Column("state_province")
    private String poiAddressStateProvince;
    @Column("postal_code")
    private String poiAddressPostalCode;
    @Column("country")
    private String poiAddressCountry;
    @Column("informal_address")
    private String poiAddressInformalAddress;

    @Column("phone_number")
    private String phoneNumber;

    @Column("website_url")
    private String websiteUrl;

    @Column("poi_amenities") // TEXT[]
    private List<String> poiAmenities;

    @Column("poi_keywords") // TEXT[]
    private List<String> poiKeywords;

    @Column("poi_type_tags") // TEXT[]
    private List<String> poiTypeTags;

    @Column("operation_time_plan") // JSONB, map to String
    private String operationTimePlan;

    @Column("poi_contacts") // contact_person_type[], map to JSON String
    private String poiContactsJson;

    @Column("popularity_score")
    private Float popularityScore;

    @Column("is_active")
    private Boolean isActive;

    @Column("deactivation_reason")
    private String deactivationReason;

    @Column("deactivated_by_user_id")
    private UUID deactivatedByUserId;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("updated_by_user_id")
    private UUID updatedByUserId;

    @Column("updated_at")
    private OffsetDateTime updatedAt;
}
