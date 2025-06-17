package com.poi.yow_point.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "organization_id", updatable = false, nullable = false)
    private UUID organizationId;

    @Column(name = "org_name", nullable = false)
    private String orgName;

    @Column(name = "org_code", unique = true)
    private String orgCode;

    @Column(name = "org_type")
    private String orgType;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private boolean isActive = true;

    @OneToMany(mappedBy = "organization")
    private Set<AppUser> users;

    @OneToMany(mappedBy = "organization")
    private Set<PointOfInterest> pois;

    @OneToMany(mappedBy = "organization")
    private Set<PoiReview> poiReviews;

    @OneToMany(mappedBy = "organization")
    private Set<PoiAccessLog> poiAccessLogs;

    @OneToMany(mappedBy = "organization")
    private Set<PoiPlatformStat> poiPlatformStats;
}
