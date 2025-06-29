package com.poi.yow_point.repositories;

//import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.models.PointOfInterest;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface PointOfInterestRepository extends R2dbcRepository<PointOfInterest, UUID> {

    /**
     * Trouve tous les POIs actifs d'une organisation
     */
    @Query("SELECT * FROM point_of_interest WHERE organization_id = :organizationId AND is_active = true ORDER BY poi_name")
    Flux<PointOfInterest> findActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * Trouve tous les POIs d'une organisation (actifs et inactifs)
     */
    @Query("SELECT * FROM point_of_interest WHERE organization_id = :organizationId ORDER BY poi_name")
    Flux<PointOfInterest> findByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * Trouve les POIs par type
     */
    @Query("SELECT * FROM point_of_interest WHERE poi_type = :poiType AND is_active = true ORDER BY poi_name")
    Flux<PointOfInterest> findByPoiType(@Param("poiType") String poiType);

    /**
     * Trouve les POIs par catégorie
     */
    @Query("SELECT * FROM point_of_interest WHERE poi_category = :poiCategory AND is_active = true ORDER BY poi_name")
    Flux<PointOfInterest> findByPoiCategory(@Param("poiCategory") String poiCategory);

    /**
     * Recherche par nom (recherche partielle, insensible à la casse)
     */
    @Query("SELECT * FROM point_of_interest WHERE LOWER(poi_name) LIKE LOWER(CONCAT('%', :name, '%')) AND is_active = true ORDER BY poi_name")
    Flux<PointOfInterest> findByPoiNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Trouve les POIs dans un rayon géographique
     */
    @Query("""
            SELECT * FROM point_of_interest
            WHERE is_active = true
            AND latitude IS NOT NULL
            AND longitude IS NOT NULL
            AND (
                6371 * acos(
                    cos(radians(:latitude)) * cos(radians(latitude)) *
                    cos(radians(longitude) - radians(:longitude)) +
                    sin(radians(:latitude)) * sin(radians(latitude))
                )
            ) <= :radiusKm
            ORDER BY (
                6371 * acos(
                    cos(radians(:latitude)) * cos(radians(latitude)) *
                    cos(radians(longitude) - radians(:longitude)) +
                    sin(radians(:latitude)) * sin(radians(latitude))
                )
            )
            """)
    Flux<PointOfInterest> findByLocationWithinRadius(
            @Param("latitude") BigDecimal latitude,
            @Param("longitude") BigDecimal longitude,
            @Param("radiusKm") Double radiusKm);

    /**
     * Trouve les POIs par ville
     */
    @Query("SELECT * FROM point_of_interest WHERE LOWER(address_city) = LOWER(:city) AND is_active = true ORDER BY poi_name")
    Flux<PointOfInterest> findByCity(@Param("city") String city);

    /**
     * Recherche complexe avec plusieurs critères
     */
    @Query("""
            SELECT * FROM point_of_interest
            WHERE is_active = true
            AND (:organizationId IS NULL OR organization_id = :organizationId)
            AND (:poiType IS NULL OR poi_type = :poiType)
            AND (:poiCategory IS NULL OR poi_category = :poiCategory)
            AND (:city IS NULL OR LOWER(address_city) = LOWER(:city))
            AND (:searchTerm IS NULL OR
                 LOWER(poi_name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
                 LOWER(poi_description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
            ORDER BY popularity_score DESC, poi_name
            """)
    Flux<PointOfInterest> findWithFilters(
            @Param("organizationId") UUID organizationId,
            @Param("poiType") String poiType,
            @Param("poiCategory") String poiCategory,
            @Param("city") String city,
            @Param("searchTerm") String searchTerm);

    /**
     * Compte les POIs actifs d'une organisation
     */
    @Query("SELECT COUNT(*) FROM point_of_interest WHERE organization_id = :organizationId AND is_active = true")
    Mono<Long> countActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    /**
     * Trouve les POIs les plus populaires
     */
    @Query("SELECT * FROM point_of_interest WHERE is_active = true ORDER BY popularity_score DESC LIMIT :limit")
    Flux<PointOfInterest> findTopByPopularityScore(@Param("limit") Integer limit);

    /**
     * Désactive un POI (soft delete)
     */
    @Query("UPDATE point_of_interest SET is_active = false, updated_at = NOW() WHERE poi_id = :poiId")
    Mono<Integer> deactivateById(@Param("poiId") UUID poiId);

    /**
     * Réactive un POI
     */
    @Query("UPDATE point_of_interest SET is_active = true, updated_at = NOW() WHERE poi_id = :poiId")
    Mono<Integer> activateById(@Param("poiId") UUID poiId);

    /**
     * Met à jour le score de popularité
     */
    @Query("UPDATE point_of_interest SET popularity_score = :score, updated_at = NOW() WHERE poi_id = :poiId")
    Mono<Integer> updatePopularityScore(@Param("poiId") UUID poiId, @Param("score") Float score);

    /**
     * Trouve les POIs créés par un utilisateur spécifique
     */
    @Query("SELECT * FROM point_of_interest WHERE created_by_user_id = :userId ORDER BY created_at DESC")
    Flux<PointOfInterest> findByCreatedByUserId(@Param("userId") UUID userId);

    /**
     * Vérifie l'existence d'un POI par nom et organisation
     */
    @Query("SELECT COUNT(*) > 0 FROM point_of_interest WHERE LOWER(poi_name) = LOWER(:name) AND organization_id = :organizationId AND poi_id != :excludeId")
    Mono<Boolean> existsByNameAndOrganizationIdExcludingId(
            @Param("name") String name,
            @Param("organizationId") UUID organizationId,
            @Param("excludeId") UUID excludeId);

}