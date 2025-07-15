package com.poi.yow_point.application.mappers;

import com.poi.yow_point.infrastructure.entities.PointOfInterest;
import com.poi.yow_point.presentation.dto.PointOfInterestDTO;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = "spring", uses = { MapperUtils.class })
public interface PointOfInterestMapper {

    /**
     * Convertit une entité PointOfInterest en DTO.
     */
    @Mapping(source = "location", target = "latitude", qualifiedByName = "pointToLatitude")
    @Mapping(source = "location", target = "longitude", qualifiedByName = "pointToLongitude")
    @Mapping(source = "operationTimePlan", target = "operationTimePlan")
    @Mapping(source = "poiContacts", target = "poiContacts")
    @Mapping(source = "poiImagesUrls", target = "poiImagesUrls") // stringToList sera utilisé
    @Mapping(source = "poiAmenities", target = "poiAmenities") // stringToList sera utilisé
    @Mapping(source = "poiKeywords", target = "poiKeywords") // stringToList sera utilisé
    PointOfInterestDTO toDto(PointOfInterest entity);

    /**
     * Convertit un DTO en entité PointOfInterest pour la création.
     */
    @Mapping(target = "poiId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "operationTimePlan", target = "operationTimePlan")
    @Mapping(source = "poiContacts", target = "poiContacts")
    @Mapping(target = "location", expression = "java(mapperUtils.coordinatesToPoint(dto.getLatitude(), dto.getLongitude()))")
    @Mapping(target = "poiImagesUrls", ignore = true)
    @Mapping(target = "poiAmenities", ignore = true)
    @Mapping(target = "poiKeywords", ignore = true)
    PointOfInterest toEntity(PointOfInterestDTO dto, @Context MapperUtils mapperUtils);

    /**
     * Met à jour une entité existante à partir des données d'un DTO.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "poiId", ignore = true)
    @Mapping(target = "createdByUserId", ignore = true)
    @Mapping(target = "organizationId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "operationTimePlan", target = "operationTimePlan")
    @Mapping(source = "poiContacts", target = "poiContacts")
    @Mapping(target = "location", expression = "java(mapperUtils.coordinatesToPoint(dto.getLatitude(), dto.getLongitude()))")
    @Mapping(target = "poiImagesUrls", ignore = true)
    @Mapping(target = "poiAmenities", ignore = true)
    @Mapping(target = "poiKeywords", ignore = true)
    void updateEntityFromDto(@MappingTarget PointOfInterest entity, PointOfInterestDTO dto,
            @Context MapperUtils mapperUtils);

    @AfterMapping
    default void setUpdateTimestamp(@MappingTarget PointOfInterest entity) {
        // La logique de mise à jour du timestamp est maintenant dans une méthode
        // `default`
        entity.setUpdatedAt(Instant.now());
    }
}