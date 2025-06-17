package com.yowyob.yowyob_point_of_interest_api.mapper;

import com.yowyob.yowyob_point_of_interest_api.model.PoiPlatformStat;
import com.yowyob.yowyob_point_of_interest_api.dto.PoiPlatformStatDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;

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
