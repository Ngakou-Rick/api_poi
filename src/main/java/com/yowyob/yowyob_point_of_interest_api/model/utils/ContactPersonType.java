package com.yowyob.yowyob_point_of_interest_api.model.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactPersonType {
    private String name;
    private String role;
    private String phone;
    private String email;
}
