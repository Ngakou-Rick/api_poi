package com.poi.yow_point.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
//import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.poi.yow_point.dto.PoiReviewDTO;
import com.poi.yow_point.models.PoiReview;

@Mapper(componentModel = "spring")
public interface PoiReviewMapper {
    PoiReviewMapper INSTANCE = Mappers.getMapper(PoiReviewMapper.class);

    // Mapping direct car l'entité contient maintenant directement les IDs
    PoiReviewDTO toDTO(PoiReview poiReview);

    // Mapping direct également
    @Mapping(target = "reviewId", ignore = true)
    PoiReview toEntity(PoiReviewDTO poiReviewDTO);
}