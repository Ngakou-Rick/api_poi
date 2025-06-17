package com.poi.yow_point.models;



import com.poi.yow_point.models.embeddable.AddressType;
import com.poi.yow_point.models.embeddable.ContactPersonType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "point_of_interest")
public class PointOfInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "poi_id", updatable = false, nullable = false)
    private UUID poiId;

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;
    
    // Assuming town_id might be nullable or handled elsewhere if it's a strict FK
    @Column(name = "town_id") 
    private UUID townId; 

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private AppUser createdBy;

    @Column(name = "poi_name", nullable = false)
    private String poiName;

    @Column(name = "poi_type", nullable = false)
    private String poiType; // Ex: 'RESTAURANT', 'HOTEL'

    @Column(name = "poi_category", nullable = false)
    private String poiCategory; // Ex: 'Food & Drink', 'Transport'

    @Column(name = "poi_long_name")
    private String poiLongName;

    @Column(name = "poi_short_name")
    private String poiShortName;

    @Column(name = "poi_friendly_name")
    private String poiFriendlyName;

    @Lob
    @Column(name = "poi_description")
    private String poiDescription;

    @Lob
    @Column(name = "poi_logo")
    private byte[] poiLogo; // For small images/logo

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "poi_images", columnDefinition = "text[]")
    private List<String> poiImages; // Tableau d'URLs

    // TODO: Implement proper PostGIS GEOGRAPHY(Point, 4326) handling. Using String for now.
    @Column(name = "location_geog", nullable = false)
    private String locationGeog; 

    @Embedded
    private AddressType poiAddress; // Adresse structurée

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "website_url")
    private String websiteUrl;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "poi_amenities", columnDefinition = "text[]")
    private List<String> poiAmenities; // Ex: ['Wi-Fi', 'Parking', "Jardin"]

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "poi_keywords", columnDefinition = "text[]")
    private List<String> poiKeywords; // Ex: ["BUS STOP", "HOPITAL", "CENTRE VILLE"]

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "poi_type_tags", columnDefinition = "text[]")
    private List<String> poiTypeTags; // Tags complémentaires

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "operation_time_plan", columnDefinition = "jsonb")
    private String operationTimePlan; // Using String for JSONB for now

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "poi_contacts_collection", joinColumns = @JoinColumn(name = "poi_id")) // Intermediate table for the collection
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "contact_name")),
            @AttributeOverride(name = "role", column = @Column(name = "contact_role")),
            @AttributeOverride(name = "phone", column = @Column(name = "contact_phone")),
            @AttributeOverride(name = "email", column = @Column(name = "contact_email"))
    })
    private List<ContactPersonType> poiContacts;


    @Column(name = "popularity_score", columnDefinition = "FLOAT DEFAULT 0")
    @Builder.Default
    private Float popularityScore = 0.0f;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "deactivation_reason")
    private String deactivationReason;

    @ManyToOne
    @JoinColumn(name = "deactivated_by_user_id")
    private AppUser deactivatedBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "updated_by_user_id")
    private AppUser updatedBy;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();
    
    @OneToMany(mappedBy = "pointOfInterest")
    private Set<PoiReview> poiReviews;

    @OneToMany(mappedBy = "pointOfInterest")
    private Set<PoiAccessLog> poiAccessLogs;

    @OneToMany(mappedBy = "pointOfInterest")
    private Set<PoiPlatformStat> poiPlatformStats;
}
