package com.poi.yow_point.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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
    @Column(name = "organization_id")
    private UUID organizationId;

    @NotBlank
    @Column(name = "org_name", nullable = false)
    private String orgName;

    @NotBlank
    @Column(name = "org_code", unique = true, nullable = false)
    private String orgCode;

    @Column(name = "org_type")
    private String orgType;

    @NotNull
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Relation inverse si vous voulez accéder aux utilisateurs ou POI de cette organisation
    // @OneToMany(mappedBy = "organization")
    // private List<AppUser> users;

    // @OneToMany(mappedBy = "organization")
    // private List<PointOfInterest> pois;
}