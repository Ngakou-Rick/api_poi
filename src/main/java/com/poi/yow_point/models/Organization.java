package com.poi.yow_point.models;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("organization")
public class Organization {

    @Id
    @Column("organization_id")
    private UUID organizationId;

    @Column("org_name")
    private String orgName;

    @Column("org_code")
    private String orgCode;

    @Column("org_type")
    private String orgType;

    @CreatedDate
    @Column("created_at")
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column("is_active")
    @Builder.Default
    private Boolean isActive = true;

    // Note: Dans Spring Data R2DBC, les relations OneToMany ne sont pas supportées
    // de la même manière
    // que dans JPA. Les relations doivent être gérées manuellement via des requêtes
    // séparées
    // ou en utilisant des services dédiés pour charger les entités liées.
}