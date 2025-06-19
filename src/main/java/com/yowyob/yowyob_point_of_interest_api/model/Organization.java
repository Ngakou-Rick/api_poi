package com.yowyob.yowyob_point_of_interest_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;
// Removed Set imports as fields are removed

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("organization") // Spring Data R2DBC
public class Organization {

    @Id // Spring Data
    @Column("organization_id")
    private UUID organizationId; // Ensure this is set before save if DB doesn't auto-generate

    @Column("org_name")
    private String orgName;

    @Column("org_code")
    private String orgCode;

    @Column("org_type")
    private String orgType;

    @Column("created_at")
    private OffsetDateTime createdAt; // Logic to set this will be in service

    @Column("is_active")
    private Boolean isActive; // Logic to default this will be in service
}
