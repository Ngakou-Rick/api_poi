package com.yowyob.yowyob_point_of_interest_api.mapper;

import com.yowyob.yowyob_point_of_interest_api.model.Organization;
import com.yowyob.yowyob_point_of_interest_api.dto.OrganizationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface OrganizationMapper {
    OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

    OrganizationDTO toDTO(Organization organization);
    Organization toEntity(OrganizationDTO organizationDTO);
}
