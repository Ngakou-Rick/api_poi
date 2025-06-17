package com.yowyob.yowyob_point_of_interest_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactPersonTypeDTO {
    private String name;
    private String role;
    private String phone;
    private String email;
}
