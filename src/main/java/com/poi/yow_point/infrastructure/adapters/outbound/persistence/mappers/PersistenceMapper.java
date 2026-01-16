package com.poi.yow_point.infrastructure.adapters.outbound.persistence.mappers;

import com.poi.yow_point.domain.model.AppUserEntity;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.AppUserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersistenceMapper {
    AppUserEntity toDomain(AppUserEntity entity);
    AppUserEntity toEntity(AppUserEntity domain);
}
