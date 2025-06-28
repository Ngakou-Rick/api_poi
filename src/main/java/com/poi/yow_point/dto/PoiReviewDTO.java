package com.poi.yow_point.dto;

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
public class PoiReviewDTO {
    private UUID reviewId;
    private UUID poiId;
    private UUID userId;
    private UUID organizationId;
    private String platformType;
    private Integer rating;
    private String reviewText;
    private OffsetDateTime createdAt;
    private Integer likes;
    private Integer dislikes;
}