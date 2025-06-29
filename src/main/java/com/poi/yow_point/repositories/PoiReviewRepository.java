package com.poi.yow_point.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.poi.yow_point.models.PoiReview;

import java.util.UUID;

@Repository
public interface PoiReviewRepository extends R2dbcRepository<PoiReview, UUID> {

    Flux<PoiReview> findByPoiId(UUID poiId);

    Flux<PoiReview> findByUserId(UUID userId);

    Flux<PoiReview> findByOrganizationId(UUID organizationId);

    Flux<PoiReview> findByPlatformType(String platformType);

    @Query("SELECT * FROM poi_review WHERE poi_id = :poiId ORDER BY created_at DESC")
    Flux<PoiReview> findByPoiIdOrderByCreatedAtDesc(@Param("poiId") UUID poiId);

    @Query("SELECT * FROM poi_review WHERE user_id = :userId ORDER BY created_at DESC")
    Flux<PoiReview> findByUserIdOrderByCreatedAtDesc(@Param("userId") UUID userId);

    @Query("SELECT AVG(rating) FROM poi_review WHERE poi_id = :poiId")
    Mono<Double> findAverageRatingByPoiId(@Param("poiId") UUID poiId);

    @Query("SELECT COUNT(*) FROM poi_review WHERE poi_id = :poiId")
    Mono<Long> countByPoiId(@Param("poiId") UUID poiId);

}