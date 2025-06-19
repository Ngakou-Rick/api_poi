package com.yowyob.yowyob_point_of_interest_api.model;

// Remove jakarta.persistence.*;
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
@Table("poi_review")
public class PoiReview {

    @Id
    @Column("review_id")
    private UUID reviewId;

    @Column("poi_id")
    private UUID poiId;

    @Column("user_id")
    private UUID userId;

    @Column("organization_id")
    private UUID organizationId;

    @Column("platform_type")
    private String platformType;

    @Column("rating")
    private Integer rating;

    @Column("review_text") // TEXT in DB
    private String reviewText;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("likes")
    private Integer likes;

    @Column("dislikes")
    private Integer dislikes;
}
