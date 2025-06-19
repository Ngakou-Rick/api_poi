package com.yowyob.yowyob_point_of_interest_api.mapper;

import com.yowyob.yowyob_point_of_interest_api.model.PointOfInterest;
import com.yowyob.yowyob_point_of_interest_api.dto.PointOfInterestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;
import java.io.IOException; // Added
import java.util.Collections; // Added
import com.fasterxml.jackson.core.type.TypeReference; // Added
import com.fasterxml.jackson.databind.ObjectMapper; // Added
import com.yowyob.yowyob_point_of_interest_api.dto.ContactPersonTypeDTO; // Added
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper(componentModel = "spring",
        uses = {AddressTypeMapper.class, ContactPersonTypeMapper.class, OrganizationMapper.class, AppUserMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface PointOfInterestMapper {
    Logger log = LoggerFactory.getLogger(PointOfInterestMapper.class);
    PointOfInterestMapper INSTANCE = Mappers.getMapper(PointOfInterestMapper.class);

    @Mapping(source = "organization.organizationId", target = "orgId")
    @Mapping(source = "createdBy.userId", target = "createdByUserId")
    @Mapping(source = "deactivatedBy.userId", target = "deactivatedByUserId")
    @Mapping(source = "updatedBy.userId", target = "updatedByUserId")
    @Mapping(source = "locationGeog", target = "locationGeogWKT", qualifiedByName = "pointToWkt")
    @Mapping(expression = "java(pointOfInterest.getLocationGeog() != null ? pointOfInterest.getLocationGeog().getY() : null)", target = "latitude")
    @Mapping(expression = "java(pointOfInterest.getLocationGeog() != null ? pointOfInterest.getLocationGeog().getX() : null)", target = "longitude")
    @Mapping(source = "poiContactsJson", target = "poiContacts", qualifiedByName = "mapPoiContactsJsonToDto")
    PointOfInterestDTO toDTO(PointOfInterest pointOfInterest);

    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "deactivatedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "poiReviews", ignore = true)
    @Mapping(target = "poiAccessLogs", ignore = true)
    @Mapping(target = "poiPlatformStats", ignore = true)
    @Mapping(target = "locationGeog", ignore = true) // Service will handle this from DTO's lat/lon
    @Mapping(target = "poiContactsJson", ignore = true) // Service will handle this from DTO's List<ContactPersonTypeDTO>
    PointOfInterest toEntity(PointOfInterestDTO pointOfInterestDTO);

    List<PointOfInterestDTO> toDTOList(List<PointOfInterest> pointOfInterests);
    List<PointOfInterest> toEntityList(List<PointOfInterestDTO> pointOfInterestDTOs);

    @Named("pointToWkt")
    default String pointToWkt(Point point) {
        if (point == null) {
            return null;
        }
        WKTWriter writer = new WKTWriter();
        return writer.write(point);
    }

    @Named("wktToPoint")
    default Point wktToPoint(String wkt) {
        if (wkt == null || wkt.trim().isEmpty()) {
            return null;
        }
        WKTReader reader = new WKTReader();
        try {
            return (Point) reader.read(wkt);
        } catch (Exception e) {
            // Consider logging this error or throwing a custom mapping exception
            log.error("Error parsing WKT string to Point: {}", wkt, e);
            return null;
        }
    }

    @Named("mapPoiContactsJsonToDto")
    default List<ContactPersonTypeDTO> mapPoiContactsJsonToDto(String json) {
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper(); // Simple instantiation for default method
            return objectMapper.readValue(json, new TypeReference<List<ContactPersonTypeDTO>>() {});
        } catch (IOException e) {
            log.error("Error deserializing poiContactsJson to DTO list: {}", json, e);
            return Collections.emptyList();
        }
    }
}
