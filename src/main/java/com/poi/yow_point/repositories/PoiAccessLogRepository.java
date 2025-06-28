package com.poi.yow_point.repositories;

import com.poi.yow_point.models.PoiAccessLog;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface PoiAccessLogRepository extends R2dbcRepository<PoiAccessLog, UUID> {

    // Trouver tous les accès pour un POI spécifique
    Flux<PoiAccessLog> findByPoiId(UUID poiId);

    // Trouver tous les accès pour une organisation
    Flux<PoiAccessLog> findByOrganizationId(UUID organizationId);

    // Trouver tous les accès d'un utilisateur
    Flux<PoiAccessLog> findByUserId(UUID userId);

    // Trouver les accès par type de plateforme
    Flux<PoiAccessLog> findByPlatformType(String platformType);

    // Trouver les accès par type d'accès
    Flux<PoiAccessLog> findByAccessType(String accessType);

    // Trouver les accès dans une période donnée
    Flux<PoiAccessLog> findByAccessDatetimeBetween(OffsetDateTime startDate, OffsetDateTime endDate);

    // Compter les accès pour un POI
    @Query("SELECT COUNT(*) FROM poi_access_log WHERE poi_id = :poiId")
    Mono<Long> countByPoiId(UUID poiId);

    // Compter les accès uniques (par utilisateur) pour un POI
    @Query("SELECT COUNT(DISTINCT user_id) FROM poi_access_log WHERE poi_id = :poiId AND user_id IS NOT NULL")
    Mono<Long> countUniqueUsersByPoiId(UUID poiId);

    // Statistiques d'accès par plateforme pour un POI
    @Query("SELECT platform_type, COUNT(*) as count FROM poi_access_log WHERE poi_id = :poiId GROUP BY platform_type")
    Flux<Object[]> getAccessStatsByPlatformForPoi(UUID poiId);

    // Accès récents pour un POI (dernières 24h)
    @Query("SELECT * FROM poi_access_log WHERE poi_id = :poiId AND access_datetime > :since ORDER BY access_datetime DESC")
    Flux<PoiAccessLog> findRecentAccessByPoiId(UUID poiId, OffsetDateTime since);

    // Accès les plus fréquents par organisation
    @Query("SELECT poi_id, COUNT(*) as access_count FROM poi_access_log WHERE organization_id = :orgId GROUP BY poi_id ORDER BY access_count DESC LIMIT :limit")
    Flux<Object[]> getMostAccessedPoisByOrganization(UUID orgId, int limit);
}