package com.poi.yow_point.infrastructure.repositories.podcast;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poi.yow_point.infrastructure.entities.Podcast;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PodcastRepository extends R2dbcRepository<Podcast, UUID> {
    @Query("SELECT * FROM podcast WHERE user_id = :userId AND is_active = true ORDER BY created_at DESC")
    Flux<Podcast> findByUserIdAndIsActiveTrue(@Param("userId") UUID userId);

    @Query("SELECT * FROM podcast WHERE poi_id = :poiId AND is_active = true ORDER BY created_at DESC")
    Flux<Podcast> findByPoiIdAndIsActiveTrue(@Param("poiId") UUID poiId);

    @Query("SELECT * FROM podcast WHERE title ILIKE :title AND is_active = true ORDER BY created_at DESC")
    Flux<Podcast> findByTitleContainingIgnoreCaseAndIsActiveTrue(@Param("title") String title);

    @Query("SELECT * FROM podcast WHERE is_active = true ORDER BY created_at DESC")
    Flux<Podcast> findAllActivePodcasts();

    @Query("SELECT * FROM podcast WHERE podcast_id = :podcastId AND is_active = true")
    Mono<Podcast> findByIdAndIsActiveTrue(@Param("podcastId") UUID podcastId);

    @Query("SELECT COUNT(*) FROM podcast WHERE user_id = :userId AND is_active = true")
    Mono<Long> countByUserIdAndIsActiveTrue(@Param("userId") UUID userId);

    @Query("SELECT COUNT(*) FROM podcast WHERE poi_id = :poiId AND is_active = true")
    Mono<Long> countByPoiIdAndIsActiveTrue(@Param("poiId") UUID poiId);

    @Query("SELECT * FROM podcast WHERE duration_seconds BETWEEN :minDuration AND :maxDuration AND is_active = true ORDER BY created_at DESC")
    Flux<Podcast> findByDurationRange(@Param("minDuration") Integer minDuration,
            @Param("maxDuration") Integer maxDuration);
}
