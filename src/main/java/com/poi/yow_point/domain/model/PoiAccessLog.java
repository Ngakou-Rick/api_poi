package com.poi.yow_point.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;

public record PoiAccessLog(
    UUID accessId,
    UUID poiId,
    UUID organizationId,
    String platformType,
    UUID userId,
    String accessType,
    OffsetDateTime accessDatetime,
    JsonNode metadata
) {}
