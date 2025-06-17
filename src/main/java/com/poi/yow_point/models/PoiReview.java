package com.poi.yow_point.models;


import jakarta.persistence.*;
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
@Entity
@Table(name = "poi_review")
public class PoiReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id", updatable = false, nullable = false)
    private UUID reviewId;

    @ManyToOne
    @JoinColumn(name = "poi_id", nullable = false)
    private PointOfInterest pointOfInterest;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    
    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "platform_type", nullable = false)
    private String platformType; // Plateforme source du review

    @Column(name = "rating") // CHECK constraint (rating >= 1 AND rating <= 5) handled by DB
    private Integer rating;

    @Lob
    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "likes", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer likes = 0;

    @Column(name = "dislikes", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private Integer dislikes = 0;
}
