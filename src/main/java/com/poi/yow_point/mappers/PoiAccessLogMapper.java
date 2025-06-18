package com.poi.yow_point.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.poi.yow_point.dto.PoiAccessLogDTO;
import com.poi.yow_point.models.PoiAccessLog;

@Mapper(componentModel = "spring", 
        uses = {PointOfInterestMapper.class, OrganizationMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface PoiAccessLogMapper {
    PoiAccessLogMapper INSTANCE = Mappers.getMapper(PoiAccessLogMapper.class);

    @Mapping(source = "pointOfInterest.poiId", target = "poiId")
    @Mapping(source = "organization.organizationId", target = "organizationId")
    PoiAccessLogDTO toDTO(PoiAccessLog poiAccessLog);

    @Mapping(target = "pointOfInterest", ignore = true)
    @Mapping(target = "organization", ignore = true)
    PoiAccessLog toEntity(PoiAccessLogDTO poiAccessLogDTO);
}