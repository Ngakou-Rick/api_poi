package com.poi.yow_point.infrastructure.mappers;

import com.poi.yow_point.domain.model.Podcast;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.PodcastDTO;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PodcastEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PodcastMapper {
    PodcastDTO toDto(Podcast podcast);
    Podcast toDomain(PodcastDTO dto);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "stringToList")
    Podcast toDomain(PodcastEntity entity);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "listToString")
    PodcastEntity toEntity(Podcast podcast);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "stringToList")
    PodcastDTO toDto(PodcastEntity entity);

    @Named("stringToList")
    default List<String> stringToList(String tags) {
        if (tags == null || tags.isEmpty()) return List.of();
        return Arrays.asList(tags.split(","));
    }

    @Named("listToString")
    default String listToString(List<String> tags) {
        if (tags == null || tags.isEmpty()) return "";
        return String.join(",", tags);
    }
}
