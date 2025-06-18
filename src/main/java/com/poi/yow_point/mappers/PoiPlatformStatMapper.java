package com.poi.yow_point.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.poi.yow_point.dto.PoiPlatformStatDTO;
import com.poi.yow_point.models.PoiPlatformStat;

@Mapper(componentModel = "spring", 
        uses = {OrganizationMapper.class, PointOfInterestMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface PoiPlatformStatMapper {
    PoiPlatformStatMapper INSTANCE = Mappers.getMapper(PoiPlatformStatMapper.class);

    @Mapping(source = "organization.organizationId", target = "orgId")
    @Mapping(source = "pointOfInterest.poiId", target = "poiId")
    PoiPlatformStatDTO toDTO(PoiPlatformStat poiPlatformStat);

    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "pointOfInterest", ignore = true)
    PoiPlatformStat toEntity(PoiPlatformStatDTO poiPlatformStatDTO);
}