package com.yowyob.yowyob_point_of_interest_api.mapper;

import com.yowyob.yowyob_point_of_interest_api.model.utils.ContactPersonType;
import com.yowyob.yowyob_point_of_interest_api.dto.ContactPersonTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface ContactPersonTypeMapper {
    ContactPersonTypeMapper INSTANCE = Mappers.getMapper(ContactPersonTypeMapper.class);
    ContactPersonTypeDTO toDTO(ContactPersonType contactPersonType);
    ContactPersonType toEntity(ContactPersonTypeDTO contactPersonTypeDTO);
}
