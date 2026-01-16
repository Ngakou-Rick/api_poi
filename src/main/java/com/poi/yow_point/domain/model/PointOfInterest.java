package com.poi.yow_point.domain.model;

import java.time.Instant;
import java.util.UUID;
import org.locationtech.jts.geom.Point;

public record PointOfInterest(
    UUID poiId,
    UUID createdByUserId,
    UUID organizationId,
    String poiName,
    String poiType,
    String poiCategory,
    String poiDescription,
    Point location,
    String addressStreetNumber,
    String addressStreetName,
    String addressCity,
    String addressPostalCode,
    String addressCountry,
    String phoneNumber,
    String websiteUrl,
    String operationTimePlanJson,
    String poiContactsJson,
    String poiImagesUrls,
    String poiAmenities,
    String poiKeywords,
    Double popularityScore,
    Boolean isActive,
    Instant createdAt,
    Instant updatedAt
) {}
