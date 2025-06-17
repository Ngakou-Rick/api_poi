package com.yowyob.yowyob_point_of_interest_api.mapper;

import com.yowyob.yowyob_point_of_interest_api.model.PointOfInterest;
import com.yowyob.yowyob_point_of_interest_api.dto.PointOfInterestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.util.List;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper(componentModel = "spring", uses = {AddressTypeMapper.class, ContactPersonTypeMapper.class, OrganizationMapper.class, AppUserMapper.class})
public interface PointOfInterestMapper {
    Logger log = LoggerFactory.getLogger(PointOfInterestMapper.class);
    PointOfInterestMapper INSTANCE = Mappers.getMapper(PointOfInterestMapper.class);

    @Mapping(source = "organization.organizationId", target = "orgId")
    @Mapping(source = "createdBy.userId", target = "createdByUserId")
    @Mapping(source = "deactivatedBy.userId", target = "deactivatedByUserId")
    @Mapping(source = "updatedBy.userId", target = "updatedByUserId")
    @Mapping(source = "locationGeog", target = "locationGeog", qualifiedByName = "pointToWkt")
    PointOfInterestDTO toDTO(PointOfInterest pointOfInterest);

    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "deactivatedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "poiReviews", ignore = true)
    @Mapping(target = "poiAccessLogs", ignore = true)
    @Mapping(target = "poiPlatformStats", ignore = true)
    @Mapping(source = "locationGeog", target = "locationGeog", qualifiedByName = "wktToPoint")
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
}
