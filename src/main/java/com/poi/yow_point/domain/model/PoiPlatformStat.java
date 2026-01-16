package com.poi.yow_point.domain.model;

import java.time.LocalDate;
import java.util.UUID;

public record PoiPlatformStat(
    UUID statId,
    UUID orgId,
    UUID poiId,
    String platformType,
    LocalDate statDate,
    Integer views,
    Integer reviews,
    Integer likes,
    Integer dislikes
) {}
