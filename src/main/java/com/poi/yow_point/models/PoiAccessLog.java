package com.poi.yow_point.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "poi_access_log")
public class PoiAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "access_id", updatable = false, nullable = false)
    private UUID accessId;

    @ManyToOne
    @JoinColumn(name = "poi_id", nullable = false)
    private PointOfInterest pointOfInterest;
    
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "platform_type", nullable = false)
    private String platformType; // 'Android', 'iOS', 'Linux', 'Web', 'Windows', etc.

    @Column(name = "user_id") // Optional user
    private UUID userId; 

    @Column(name = "access_type")
    private String accessType; // 'view', 'click', 'review', etc.

    @Column(name = "access_datetime", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    @Builder.Default
    private OffsetDateTime accessDatetime = OffsetDateTime.now();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata; // Using String for JSONB for now
}

