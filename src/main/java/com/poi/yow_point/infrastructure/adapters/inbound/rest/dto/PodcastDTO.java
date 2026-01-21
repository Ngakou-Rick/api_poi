package com.poi.yow_point.infrastructure.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for Podcast")
public class PodcastDTO {
    private UUID podcastId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Audio URL is required")
    private String audioUrl;

    private Integer duration;
    private UUID authorId;
    private String imageUrl;
    private List<String> tags;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
