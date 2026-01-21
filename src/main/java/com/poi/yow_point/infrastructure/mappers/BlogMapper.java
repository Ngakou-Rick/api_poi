package com.poi.yow_point.infrastructure.mappers;

import com.poi.yow_point.domain.model.Blog;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.BlogDTO;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.BlogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    BlogDTO toDto(Blog blog);
    Blog toDomain(BlogDTO dto);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "stringToList")
    Blog toDomain(BlogEntity entity);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "listToString")
    BlogEntity toEntity(Blog blog);

    @Mapping(target = "tags", source = "tags", qualifiedByName = "stringToList")
    BlogDTO toDto(BlogEntity entity);

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
