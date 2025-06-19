package com.yowyob.yowyob_point_of_interest_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private UUID userId;
    private UUID orgId; // To represent the organization relationship
    private String username;
    private String email;
    private String phone;
    // Password hash should not be in DTOs exposed to client
    private String role;
    private Boolean isActive;
    private OffsetDateTime createdAt;
}
