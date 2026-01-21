package com.poi.yow_point.infrastructure.mappers;

import com.poi.yow_point.domain.model.Blog;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.BlogDTO;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.BlogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    BlogDTO toDto(Blog blog);
    Blog toDomain(BlogDTO dto);
    Blog toDomain(BlogEntity entity);
    BlogEntity toEntity(Blog blog);
    BlogDTO toDto(BlogEntity entity);
}
