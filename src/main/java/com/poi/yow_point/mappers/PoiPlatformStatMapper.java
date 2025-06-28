package com.poi.yow_point.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.CollectionMappingStrategy;

import com.poi.yow_point.dto.PoiPlatformStatDTO;
import com.poi.yow_point.models.PoiPlatformStat;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface PoiPlatformStatMapper {

    // Mapping pour l'entité réactive (utilise directement les UUID)
    @Mapping(source = "orgId", target = "orgId")
    @Mapping(source = "poiId", target = "poiId")
    PoiPlatformStatDTO toDTO(PoiPlatformStat poiPlatformStat);

    // Mapping inverse
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "pointOfInterest", ignore = true)
    PoiPlatformStat toEntity(PoiPlatformStatDTO poiPlatformStatDTO);

    // Méthodes utilitaires pour le mapping avec les objets relationnels
    @Mapping(source = "organization.organizationId", target = "orgId")
    @Mapping(source = "pointOfInterest.poiId", target = "poiId")
    PoiPlatformStatDTO toDTOWithRelations(PoiPlatformStat poiPlatformStat);
}