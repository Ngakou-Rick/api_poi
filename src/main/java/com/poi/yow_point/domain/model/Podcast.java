package com.poi.yow_point.domain.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record Podcast(
    UUID podcastId,
    String title,
    String description,
    String audioUrl,
    Integer duration,
    UUID authorId,
    String imageUrl,
    List<String> tags,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
