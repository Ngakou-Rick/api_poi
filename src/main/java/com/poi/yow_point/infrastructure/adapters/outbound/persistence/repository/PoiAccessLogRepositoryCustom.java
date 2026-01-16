package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repositoryAccessLog;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PoiAccessLogEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PoiAccessLogRepositoryCustom {

    Flux<PoiAccessLogEntity> findByPoiId(UUID poiId);

    Flux<PoiAccessLogEntity> findByOrganizationId(UUID organizationId);

    Flux<PoiAccessLogEntity> findByUserId(UUID userId);

    Flux<PoiAccessLogEntity> findByAccessType(String accessType);

    Flux<PoiAccessLogEntity> findByPlatformType(String platformType);

    Flux<PoiAccessLogEntity> findByPoiIdAndOrganizationId(UUID poiId, UUID organizationId);

    Flux<PoiAccessLogEntity> findByAccessDatetimeBetween(OffsetDateTime startDate, OffsetDateTime endDate);

    Flux<PoiAccessLogEntity> findRecentByPoiId(UUID poiId, OffsetDateTime since);

    Mono<Long> countByPoiId(UUID poiId);

    Mono<Long> countByPoiIdAndAccessType(UUID poiId, String accessType);

    Mono<Long> deleteOldLogs(OffsetDateTime beforeDate);

    Flux<PoiAccessLogEntity> findByPoiIdWithPagination(UUID poiId, int limit, int offset);

    Flux<Map<String, Object>> getPlatformStatsForOrganization(UUID organizationId);
}
