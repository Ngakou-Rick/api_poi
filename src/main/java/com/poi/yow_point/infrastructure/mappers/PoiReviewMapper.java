package com.poi.yow_point.infrastructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
//import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PoiReviewEntity;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.PoiReviewDTO;

@Mapper(componentModel = "spring")
public interface PoiReviewMapper {
    PoiReviewMapper INSTANCE = Mappers.getMapper(PoiReviewMapper.class);

    // Mapping direct car l'entité contient maintenant directement les IDs
    PoiReviewDTO toDTO(PoiReviewEntity poiReview);

    // Mapping direct également
    @Mapping(target = "reviewId", ignore = true)
    PoiReviewEntity toEntity(PoiReviewDTO poiReviewDTO);
}