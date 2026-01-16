package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repositoryReview;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PoiReviewEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PoiReviewRepositoryCustom {
    Flux<PoiReviewEntity> findByPoiId(UUID poiId);

    Flux<PoiReviewEntity> findByUserId(UUID userId);

    Flux<PoiReviewEntity> findByOrganizationId(UUID organizationId);

    Flux<PoiReviewEntity> findByPlatformType(String platformType);

    Flux<PoiReviewEntity> findByPoiIdOrderByCreatedAtDesc(UUID poiId);

    Flux<PoiReviewEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Mono<Double> findAverageRatingByPoiId(UUID poiId);

    Mono<Long> countByPoiId(UUID poiId);
}