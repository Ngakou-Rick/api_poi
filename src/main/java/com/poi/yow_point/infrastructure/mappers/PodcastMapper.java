package com.poi.yow_point.infrastructure.mappers;

import com.poi.yow_point.domain.model.Podcast;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.PodcastDTO;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PodcastEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PodcastMapper {
    PodcastDTO toDto(Podcast podcast);
    Podcast toDomain(PodcastDTO dto);
    Podcast toDomain(PodcastEntity entity);
    PodcastEntity toEntity(Podcast podcast);
    PodcastDTO toDto(PodcastEntity entity);
}
