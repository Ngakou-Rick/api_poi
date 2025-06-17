package com.yowyob.yowyob_point_of_interest_api.mapper;

import com.yowyob.yowyob_point_of_interest_api.model.PoiAccessLog;
import com.yowyob.yowyob_point_of_interest_api.dto.PoiAccessLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;

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
