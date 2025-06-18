package com.poi.yow_point.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.poi.yow_point.dto.ContactPersonTypeDTO;
import com.poi.yow_point.models.embeddable.ContactPersonType;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface ContactPersonTypeMapper {
    ContactPersonTypeMapper INSTANCE = Mappers.getMapper(ContactPersonTypeMapper.class);
    ContactPersonTypeDTO toDTO(ContactPersonType contactPersonType);
    ContactPersonType toEntity(ContactPersonTypeDTO contactPersonTypeDTO);
}
