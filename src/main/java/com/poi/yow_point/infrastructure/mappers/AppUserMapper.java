package com.poi.yow_point.infrastructure.mappers;

import com.poi.yow_point.domain.model.AppUser;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.AppUserEntity;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.AppUserDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface AppUserMapper {

    // Mapping pour la lecture : exclut le passwordHash
    @Mapping(target = "password", ignore = true)
    AppUserDTO toDTO(AppUserEntity appUser);

    // Mapping pour la création : ignore les champs auto-générés et le password
    // (sera géré séparément)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    AppUserEntity toEntity(AppUserDTO appUserDTO);

    // Mapping pour la mise à jour : ignore les champs non modifiables et le
    // password
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(AppUserDTO dto, @MappingTarget AppUserEntity entity);

    // Mapping pour la création avec mot de passe
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "passwordHash", source = "passwordHash")
    AppUserEntity toEntityWithPassword(AppUserDTO appUserDTO, String passwordHash);

    // Mapping entre AppUser (domain) et DTO
    @Mapping(target = "password", ignore = true)
    AppUserDTO toDTO(AppUser appUser);
    @Mapping(target = "passwordHash", source = "passwordHash")
    AppUser toDomain(AppUserEntity entity);
    @Mapping(target = "passwordHash", source = "passwordHash")
    AppUserEntity toEntity(AppUser appUser);
}