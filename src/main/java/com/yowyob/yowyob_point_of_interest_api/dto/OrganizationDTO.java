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
public class OrganizationDTO {
    private UUID organizationId;
    private String orgName;
    private String orgCode;
    private String orgType;
    private OffsetDateTime createdAt;
    private Boolean isActive;
    // Note: Collections of related entities (users, pois, etc.) are typically not included in basic DTOs
    // to avoid overly large payloads and circular dependencies. They can be fetched via separate endpoints.
}
