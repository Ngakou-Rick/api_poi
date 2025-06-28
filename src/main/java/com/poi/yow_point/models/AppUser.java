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
@Table("app_user")
public class AppUser {

    @Id
    @Column("user_id")
    private UUID userId;

    @Column("organization_id")
    private UUID orgId; // Référence vers l'organisation (clé étrangère)

    @Column("username")
    private String username;

    @Column("email")
    private String email;

    @Column("phone")
    private String phone;

    @Column("password_hash")
    private String passwordHash;

    @Column("role")
    private String role; // e.g. 'USER', 'ADMIN', 'SUPER_ADMIN'

    @Column("is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column("created_at")
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // Note: Dans R2DBC, les relations sont gérées différemment
    // Les relations OneToMany et ManyToOne ne sont pas supportées nativement
    // Il faut gérer les relations manuellement via des services séparés
    // Exemples :
    // - Pour récupérer l'organisation : OrganizationService.findById(orgId)
    // - Pour récupérer les POIs créés : PoiService.findByCreatedBy(userId)
    // - Pour récupérer les reviews : PoiReviewService.findByUserId(userId)
}