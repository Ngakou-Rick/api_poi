package com.poi.yow_point.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.poi.yow_point.dto.AddressTypeDTO;
import com.poi.yow_point.models.embeddable.AddressType;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface AddressTypeMapper {
    AddressTypeMapper INSTANCE = Mappers.getMapper(AddressTypeMapper.class);
    AddressTypeDTO toDTO(AddressType addressType);
    AddressType toEntity(AddressTypeDTO addressTypeDTO);
}
