package com.yowyob.yowyob_point_of_interest_api.mapper;

import com.yowyob.yowyob_point_of_interest_api.model.utils.AddressType;
import com.yowyob.yowyob_point_of_interest_api.dto.AddressTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressTypeMapper {
    AddressTypeMapper INSTANCE = Mappers.getMapper(AddressTypeMapper.class);
    AddressTypeDTO toDTO(AddressType addressType);
    AddressType toEntity(AddressTypeDTO addressTypeDTO);
}
