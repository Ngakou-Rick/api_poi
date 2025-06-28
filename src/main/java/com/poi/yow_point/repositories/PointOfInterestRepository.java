package com.poi.yow_point.repositories;

import com.poi.yow_point.models.PointOfInterest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface PointOfInterestRepository extends ReactiveCrudRepository<PointOfInterest, UUID> {

        // Recherche par organisation
        Flux<PointOfInterest> findByOrganizationId(UUID organizationId);

        // Recherche par statut actif
        Flux<PointOfInterest> findByIsActive(Boolean isActive);

        // Recherche par organisation et statut
        Flux<PointOfInterest> findByOrganizationIdAndIsActive(UUID organizationId, Boolean isActive);

        // Recherche par type
        Flux<PointOfInterest> findByPoiType(String poiType);

        // Recherche par catégorie
        Flux<PointOfInterest> findByPoiCategory(String poiCategory);

        // Recherche par ville
        Flux<PointOfInterest> findByAddressCity(String city);

        // Recherche par nom (contient)
        @Query("SELECT * FROM point_of_interest WHERE LOWER(poi_name) LIKE LOWER(CONCAT('%', :name, '%'))")
        Flux<PointOfInterest> findByPoiNameContainingIgnoreCase(@Param("name") String name);

        // Recherche géographique dans un rayon
        @Query("SELECT *, " +
                        "6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " +
                        "cos(radians(longitude) - radians(:lon)) + sin(radians(:lat)) * " +
                        "sin(radians(latitude))) AS distance " +
                        "FROM point_of_interest " +
                        "WHERE 6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " +
                        "cos(radians(longitude) - radians(:lon)) + sin(radians(:lat)) * " +
                        "sin(radians(latitude))) <= :radius " +
                        "ORDER BY distance")
        Flux<PointOfInterest> findByLocationWithinRadius(
                        @Param("lat") BigDecimal latitude,
                        @Param("lon") BigDecimal longitude,
                        @Param("radius") Double radiusKm);

        // Recherche par popularité minimale
        Flux<PointOfInterest> findByPopularityScoreGreaterThanEqual(Float minScore);

        // Recherche combinée par organisation, type et statut
        @Query("SELECT * FROM point_of_interest " +
                        "WHERE organization_id = :orgId " +
                        "AND poi_type = :type " +
                        "AND is_active = :active " +
                        "ORDER BY popularity_score DESC")
        Flux<PointOfInterest> findByOrganizationAndTypeAndStatus(
                        @Param("orgId") UUID organizationId,
                        @Param("type") String poiType,
                        @Param("active") Boolean isActive);

        // Recherche par mots-clés (contient)
        @Query("SELECT * FROM point_of_interest WHERE LOWER(poi_keywords) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        Flux<PointOfInterest> findByKeywordContaining(@Param("keyword") String keyword);

        // Compter par organisation
        Mono<Long> countByOrganizationId(UUID organizationId);

        // Compter les POI actifs
        Mono<Long> countByIsActive(Boolean isActive);

        // Trouver les plus populaires
        @Query("SELECT * FROM point_of_interest WHERE is_active = true ORDER BY popularity_score DESC LIMIT :limit")
        Flux<PointOfInterest> findTopByPopularity(@Param("limit") Integer limit);
}