package com.poi.yow_point.mappers;

import com.poi.yow_point.dto.AppUserDTO;
import com.poi.yow_point.models.AppUser;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {OrganizationMapper.class}, collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface AppUserMapper {

    @Mapping(source = "organization.organizationId", target = "orgId")
    AppUserDTO toDTO(AppUser appUser);
    // For toEntity, Organization would need to be fetched or set manually in service if only orgId is in DTO
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdPois", ignore = true)
    @Mapping(target = "deactivatedPois", ignore = true)
    @Mapping(target = "updatedPois", ignore = true)
    @Mapping(target = "poiReviews", ignore = true)
    AppUser toEntity(AppUserDTO appUserDTO);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdPois", ignore = true)
    @Mapping(target = "deactivatedPois", ignore = true)
    @Mapping(target = "updatedPois", ignore = true)
    @Mapping(target = "poiReviews", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(AppUserDTO dto, @MappingTarget AppUser entity);
}
