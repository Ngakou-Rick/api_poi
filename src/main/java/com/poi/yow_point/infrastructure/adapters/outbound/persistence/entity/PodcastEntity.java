package com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("podcast")
public class PodcastEntity {
    @Id
    private UUID podcastId;
    private String title;
    private String description;
    private String audioUrl;
    private Integer duration;
    private UUID authorId;
    private String imageUrl;
    private String tags; // Store as comma-separated string
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
