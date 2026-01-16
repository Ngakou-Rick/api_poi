package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PoiPlatformStatEntity;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.UUID;

public interface PoiPlatformStatRepositoryCustom {

    Flux<PoiPlatformStatEntity> findByOrgIdAndDateRange(UUID orgId, LocalDate startDate, LocalDate endDate);

    Flux<PoiPlatformStatEntity> findByPoiIdAndPlatformTypeAndDateRange(UUID poiId, String platformType, LocalDate startDate,
            LocalDate endDate);
}