package com.poi.yow_point.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AppUser(
    UUID userId,
    UUID orgId,
    String username,
    String email,
    String phone,
    String passwordHash,
    String role,
    Boolean isActive,
    OffsetDateTime createdAt
) {}
