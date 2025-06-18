package com.poi.yow_point.mappers;

import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.models.PointOfInterest;
import org.mapstruct.BeanMapping;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring",
        uses = {AddressTypeMapper.class, ContactPersonTypeMapper.class, OrganizationMapper.class, AppUserMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface PointOfInterestMapper {

    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Mapping(source = "organization.organizationId", target = "orgId")
    @Mapping(source = "createdBy.userId", target = "createdByUserId")
    @Mapping(source = "deactivatedBy.userId", target = "deactivatedByUserId")
    @Mapping(source = "updatedBy.userId", target = "updatedByUserId")
    @Mapping(source = "locationGeog.y", target = "latitude")
    @Mapping(source = "locationGeog.x", target = "longitude")
    PointOfInterestDTO toDTO(PointOfInterest pointOfInterest);

    @Mapping(target = "poiId", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "deactivatedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "poiReviews", ignore = true)
    @Mapping(target = "poiAccessLogs", ignore = true)
    @Mapping(target = "poiPlatformStats", ignore = true)
    @Mapping(target = "locationGeog", source = "dto")
    PointOfInterest toEntity(PointOfInterestDTO dto);

    List<PointOfInterestDTO> toDTOList(List<PointOfInterest> pointOfInterests);

    List<PointOfInterest> toEntityList(List<PointOfInterestDTO> pointOfInterestDTOs);

    @Mapping(target = "poiId", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "deactivatedBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "poiReviews", ignore = true)
    @Mapping(target = "poiAccessLogs", ignore = true)
    @Mapping(target = "poiPlatformStats", ignore = true)
    @Mapping(target = "locationGeog", source = "dto")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePoiFromDto(PointOfInterestDTO dto, @MappingTarget PointOfInterest entity);

    default String bytesToB64(byte[] bytes) {
        return (bytes != null) ? Base64.getEncoder().encodeToString(bytes) : null;
    }

    default byte[] b64ToBytes(String b64) {
        return (b64 != null) ? Base64.getDecoder().decode(b64) : null;
    }

    default Point toPoint(PointOfInterestDTO dto) {
        if (dto == null || dto.getLongitude() == null || dto.getLatitude() == null) {
            return null;
        }
        return geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
    }
}