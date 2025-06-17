package com.yowyob.yowyob_point_of_interest_api.mapper;

import com.yowyob.yowyob_point_of_interest_api.model.AppUser;
import com.yowyob.yowyob_point_of_interest_api.dto.AppUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {OrganizationMapper.class}, collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface AppUserMapper {
    AppUserMapper INSTANCE = Mappers.getMapper(AppUserMapper.class);

    @Mapping(source = "organization.organizationId", target = "orgId")
    AppUserDTO toDTO(AppUser appUser);
    // For toEntity, Organization would need to be fetched or set manually in service if only orgId is in DTO
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdPois", ignore = true)
    @Mapping(target = "deactivatedPois", ignore = true)
    @Mapping(target = "updatedPois", ignore = true)
    @Mapping(target = "poiReviews", ignore = true)
    AppUser toEntity(AppUserDTO appUserDTO);
}
