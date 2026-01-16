package com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity;

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
public class AppUserEntity {

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

    // Getters and Setters
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public UUID getOrgId() { return orgId; }
    public void setOrgId(UUID orgId) { this.orgId = orgId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}