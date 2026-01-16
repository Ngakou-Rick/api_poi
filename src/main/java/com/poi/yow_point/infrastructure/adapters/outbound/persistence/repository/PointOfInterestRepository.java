package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
//import org.springframework.stereotype.Repository;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PointOfInterestEntity;

import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PointOfInterestRepository
                extends R2dbcRepository<PointOfInterestEntity, UUID>, PointOfInterestRepositoryCustom {

        Flux<PointOfInterestEntity> findActiveByOrganizationId(UUID organizationId);

        Flux<PointOfInterestEntity> findByOrganizationId(UUID organizationId);

        Flux<PointOfInterestEntity> findByPoiType(String poiType);

        Flux<PointOfInterestEntity> findByPoiCategory(String poiCategory);

        Flux<PointOfInterestEntity> findByPoiNameContainingIgnoreCase(String poiName);

        Flux<PointOfInterestEntity> findByAddressCity(String addressCity);

        Flux<PointOfInterestEntity> findByCreatedByUserId(UUID userId);

}