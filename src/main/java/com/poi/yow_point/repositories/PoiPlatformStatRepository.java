package com.poi.yow_point.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.poi.yow_point.models.PoiPlatformStat;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface PoiPlatformStatRepository extends R2dbcRepository<PoiPlatformStat, UUID> {

    // Méthodes de recherche réactives personnalisées
    Flux<PoiPlatformStat> findByOrgId(UUID orgId);

    Flux<PoiPlatformStat> findByPoiId(UUID poiId);

    Flux<PoiPlatformStat> findByPlatformType(String platformType);

    Flux<PoiPlatformStat> findByStatDate(LocalDate statDate);

    Flux<PoiPlatformStat> findByOrgIdAndStatDate(UUID orgId, LocalDate statDate);

    Flux<PoiPlatformStat> findByPoiIdAndStatDate(UUID poiId, LocalDate statDate);

    Flux<PoiPlatformStat> findByOrgIdAndPlatformType(UUID orgId, String platformType);

    Flux<PoiPlatformStat> findByStatDateBetween(LocalDate startDate, LocalDate endDate);

    // Requêtes personnalisées avec @Query
    @Query("SELECT * FROM poi_platform_stat WHERE org_id = :orgId AND stat_date BETWEEN :startDate AND :endDate")
    Flux<PoiPlatformStat> findByOrgIdAndDateRange(UUID orgId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT * FROM poi_platform_stat WHERE poi_id = :poiId AND platform_type = :platformType AND stat_date BETWEEN :startDate AND :endDate")
    Flux<PoiPlatformStat> findByPoiIdAndPlatformTypeAndDateRange(UUID poiId, String platformType, LocalDate startDate,
            LocalDate endDate);

    // Suppression par critères
    Mono<Void> deleteByOrgId(UUID orgId);

    Mono<Void> deleteByPoiId(UUID poiId);
}