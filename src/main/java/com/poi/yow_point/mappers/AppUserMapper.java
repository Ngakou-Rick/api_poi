package com.poi.yow_point.mappers;

import com.poi.yow_point.dto.AppUserDTO;
import com.poi.yow_point.models.AppUser;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface AppUserMapper {

    // Mapping direct car orgId est maintenant un champ direct dans l'entité
    AppUserDTO toDTO(AppUser appUser);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    AppUser toEntity(AppUserDTO appUserDTO);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(AppUserDTO dto, @MappingTarget AppUser entity);

    // Note: Le mapper est considérablement simplifié car :
    // 1. orgId est maintenant un champ direct (pas d'objet Organization imbriqué)
    // 2. Les relations OneToMany ne sont plus gérées dans l'entité
    // 3. Pas besoin de dépendance sur OrganizationMapper
}