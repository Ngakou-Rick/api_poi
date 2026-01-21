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
@Table("blog")
public class BlogEntity {
    @Id
    private UUID blogId;
    private String title;
    private String content;
    private UUID authorId;
    private String imageUrl;
    private String tags; // Store as comma-separated string
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
