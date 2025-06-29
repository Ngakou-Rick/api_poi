package com.poi.yow_point.repositories;

import com.poi.yow_point.models.PoiAccessLog;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface PoiAccessLogRepository extends R2dbcRepository<PoiAccessLog, UUID> {

    // Recherche par POI ID
    Flux<PoiAccessLog> findByPoiId(UUID poiId);

    // Recherche par Organization ID
    Flux<PoiAccessLog> findByOrganizationId(UUID organizationId);

    // Recherche par User ID
    Flux<PoiAccessLog> findByUserId(UUID userId);

    // Recherche par type d'accès
    Flux<PoiAccessLog> findByAccessType(String accessType);

    // Recherche par plateforme
    Flux<PoiAccessLog> findByPlatformType(String platformType);

    // Recherche par POI et organisation
    Flux<PoiAccessLog> findByPoiIdAndOrganizationId(UUID poiId, UUID organizationId);

    // Recherche par période
    @Query("SELECT * FROM poi_access_log WHERE access_datetime BETWEEN :startDate AND :endDate ORDER BY access_datetime DESC")
    Flux<PoiAccessLog> findByAccessDatetimeBetween(@Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate);

    // Recherche des logs récents pour un POI
    @Query("SELECT * FROM poi_access_log WHERE poi_id = :poiId AND access_datetime >= :since ORDER BY access_datetime DESC")
    Flux<PoiAccessLog> findRecentByPoiId(@Param("poiId") UUID poiId,
            @Param("since") OffsetDateTime since);

    // Compter les accès par POI
    @Query("SELECT COUNT(*) FROM poi_access_log WHERE poi_id = :poiId")
    Mono<Long> countByPoiId(@Param("poiId") UUID poiId);

    // Compter les accès par type pour un POI
    @Query("SELECT COUNT(*) FROM poi_access_log WHERE poi_id = :poiId AND access_type = :accessType")
    Mono<Long> countByPoiIdAndAccessType(@Param("poiId") UUID poiId,
            @Param("accessType") String accessType);

    // Supprimer les logs anciens
    @Query("DELETE FROM poi_access_log WHERE access_datetime < :beforeDate")
    Mono<Long> deleteOldLogs(@Param("beforeDate") OffsetDateTime beforeDate);

    // Recherche avec pagination (utilise LIMIT et OFFSET)
    @Query("SELECT * FROM poi_access_log WHERE poi_id = :poiId ORDER BY access_datetime DESC LIMIT :limit OFFSET :offset")
    Flux<PoiAccessLog> findByPoiIdWithPagination(@Param("poiId") UUID poiId,
            @Param("limit") int limit,
            @Param("offset") int offset);

    // Statistiques par plateforme pour une organisation
    @Query("SELECT platform_type, COUNT(*) as count FROM poi_access_log WHERE organization_id = :orgId GROUP BY platform_type")
    Flux<Object[]> getPlatformStatsForOrganization(@Param("orgId") UUID organizationId);

}