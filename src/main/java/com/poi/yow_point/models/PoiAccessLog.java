package com.poi.yow_point.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("poi_access_log")
public class PoiAccessLog {

    @Id
    @Column("access_id")
    private UUID accessId;

    @Column("poi_id")
    private UUID poiId; // Foreign key reference au lieu de @ManyToOne

    @Column("organization_id")
    private UUID organizationId; // Foreign key reference au lieu de @ManyToOne

    @Column("platform_type")
    private String platformType; // 'Android', 'iOS', 'Linux', 'Web', 'Windows', etc.

    @Column("user_id")
    private UUID userId; // Optional user

    @Column("access_type")
    private String accessType; // 'view', 'click', 'review', etc.

    @CreatedDate
    @Column("access_datetime")
    @Builder.Default
    private OffsetDateTime accessDatetime = OffsetDateTime.now();

    @Column("metadata")
    private String metadata; // JSONB stocké comme String

    // Méthodes utilitaires pour la gestion des métadonnées JSON
    public boolean hasMetadata() {
        return metadata != null && !metadata.trim().isEmpty();
    }

}