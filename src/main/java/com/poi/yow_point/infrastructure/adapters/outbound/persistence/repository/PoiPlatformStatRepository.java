package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
//import org.springframework.stereotype.Repository;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PoiPlatformStatEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface PoiPlatformStatRepository
        extends R2dbcRepository<PoiPlatformStatEntity, UUID>, PoiPlatformStatRepositoryCustom {

    // Méthodes de recherche réactives personnalisées
    Flux<PoiPlatformStatEntity> findByOrgId(UUID orgId);

    Flux<PoiPlatformStatEntity> findByPoiId(UUID poiId);

    Flux<PoiPlatformStatEntity> findByPlatformType(String platformType);

    Flux<PoiPlatformStatEntity> findByStatDate(LocalDate statDate);

    Flux<PoiPlatformStatEntity> findByOrgIdAndStatDate(UUID orgId, LocalDate statDate);

    Flux<PoiPlatformStatEntity> findByPoiIdAndStatDate(UUID poiId, LocalDate statDate);

    Flux<PoiPlatformStatEntity> findByOrgIdAndPlatformType(UUID orgId, String platformType);

    Flux<PoiPlatformStatEntity> findByStatDateBetween(LocalDate startDate, LocalDate endDate);

    // Suppression par critères
    Mono<Void> deleteByOrgId(UUID orgId);

    Mono<Void> deleteByPoiId(UUID poiId);
}