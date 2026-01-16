package com.poi.yow_point.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PoiReview(
    UUID reviewId,
    UUID poiId,
    UUID userId,
    Integer rating,
    String reviewText,
    OffsetDateTime createdAt
) {}
