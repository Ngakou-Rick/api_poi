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
// Removed Set imports

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("app_user") // Spring Data R2DBC
public class AppUser {

    @Id // Spring Data
    @Column("user_id")
    private UUID userId; // Ensure this is set before save

    @Column("org_id")
    private UUID orgId; // Foreign key

    @Column("username")
    private String username;

    @Column("email")
    private String email;

    @Column("phone")
    private String phone;

    @Column("password_hash")
    private String passwordHash;

    @Column("role")
    private String role;

    @Column("is_active")
    private Boolean isActive; // Logic to default this will be in service

    @Column("created_at")
    private OffsetDateTime createdAt; // Logic to set this will be in service
}
