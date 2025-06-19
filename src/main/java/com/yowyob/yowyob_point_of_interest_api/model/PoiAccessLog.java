package com.yowyob.yowyob_point_of_interest_api.model;

// Remove jakarta.persistence.*;
// Remove org.hibernate.annotations.JdbcTypeCode;
// Remove org.hibernate.type.SqlTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
    private UUID poiId;

    @Column("organization_id")
    private UUID organizationId;

    @Column("platform_type")
    private String platformType;

    @Column("user_id")
    private UUID userId;

    @Column("access_type")
    private String accessType;

    @Column("access_datetime")
    private OffsetDateTime accessDatetime;

    @Column("metadata") // JSONB, map to String
    private String metadata;
}
