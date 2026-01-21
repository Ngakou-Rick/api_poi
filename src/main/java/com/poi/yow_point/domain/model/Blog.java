package com.poi.yow_point.domain.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record Blog(
    UUID blogId,
    String title,
    String content,
    UUID authorId,
    String imageUrl,
    List<String> tags,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
