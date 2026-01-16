package com.poi.yow_point.infrastructure.adapters.inbound.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressTypeDTO {
    private String streetNumber;
    private String streetName;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String informalAddress;
}