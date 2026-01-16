package com.poi.yow_point.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Organization(
    UUID organizationId,
    String orgName,
    String orgCode,
    String orgType,
    String address,
    String email,
    String phone,
    Boolean isActive,
    OffsetDateTime createdAt
) {}
