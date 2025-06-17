package com.yowyob.yowyob_point_of_interest_api.model.utils;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AddressType {
    private String streetNumber;
    private String streetName;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String informalAddress;
}
