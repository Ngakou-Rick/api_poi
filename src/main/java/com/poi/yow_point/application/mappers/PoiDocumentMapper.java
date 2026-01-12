package com.poi.yow_point.application.mappers;

import com.poi.yow_point.infrastructure.elasticsearch.document.PoiDocument;
import com.poi.yow_point.presentation.dto.PointOfInterestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Mapper(componentModel = "spring", imports = GeoPoint.class)
public interface PoiDocumentMapper {

    @Mapping(target = "id", source = "poiId")
    @Mapping(target = "location", expression = "java(new GeoPoint(dto.getLatitude(), dto.getLongitude()))")
    PoiDocument toDocument(PointOfInterestDTO dto);
}
