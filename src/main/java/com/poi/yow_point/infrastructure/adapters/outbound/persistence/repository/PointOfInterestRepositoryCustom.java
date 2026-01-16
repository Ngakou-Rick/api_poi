package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import java.util.UUID;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PointOfInterestEntity;

import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
import reactor.core.publisher.Mono;

public interface PointOfInterestRepositoryCustom {

    Flux<PointOfInterestEntity> findByLocationWithinRadius(Double latitude, Double longitude, Double radiusKm);

    Flux<PointOfInterestEntity> findTopByPopularityScore(Integer limit);

    Mono<Boolean> existsByNameAndOrganizationIdExcludingId(String name, UUID organizationId, UUID excludeId);

    Mono<Long> deactivateById(UUID poiId);

    Mono<Long> activateById(UUID poiId);

    Mono<Long> updatePopularityScore(UUID poiId, Float score);

    Mono<Long> countActiveByOrganizationId(UUID organizationId);

}
