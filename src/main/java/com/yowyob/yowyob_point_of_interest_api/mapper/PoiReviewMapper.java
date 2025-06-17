package com.yowyob.yowyob_point_of_interest_api.mapper;

import com.yowyob.yowyob_point_of_interest_api.model.PoiReview;
import com.yowyob.yowyob_point_of_interest_api.dto.PoiReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {PointOfInterestMapper.class, AppUserMapper.class, OrganizationMapper.class})
public interface PoiReviewMapper {
    PoiReviewMapper INSTANCE = Mappers.getMapper(PoiReviewMapper.class);

    @Mapping(source = "pointOfInterest.poiId", target = "poiId")
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "organization.organizationId", target = "organizationId")
    PoiReviewDTO toDTO(PoiReview poiReview);

    @Mapping(target = "pointOfInterest", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "organization", ignore = true)
    PoiReview toEntity(PoiReviewDTO poiReviewDTO);
}
