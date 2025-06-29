package com.poi.yow_point.services;

import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.mappers.PointOfInterestMapper;
import com.poi.yow_point.models.PointOfInterest;
import com.poi.yow_point.repositories.PointOfInterestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointOfInterestService {

    private final PointOfInterestRepository repository;
    private final PointOfInterestMapper mapper;

    /**
     * Crée un nouveau POI
     */
    @Transactional
    public Mono<PointOfInterestDTO> createPoi(PointOfInterestDTO dto) {
        return Mono.fromCallable(() -> {
            log.info("Creating new POI: {}", dto.getPoiName());

            // Validation
            if (dto.getPoiName() == null || dto.getPoiName().trim().isEmpty()) {
                throw new IllegalArgumentException("POI name cannot be null or empty");
            }
            if (dto.getOrganizationId() == null) {
                throw new IllegalArgumentException("Organization ID cannot be null");
            }

            return dto;
        })
                .flatMap(validatedDto -> {
                    // Vérification de l'unicité du nom dans l'organisation
                    return repository.existsByNameAndOrganizationIdExcludingId(
                            validatedDto.getPoiName(),
                            validatedDto.getOrganizationId(),
                            UUID.randomUUID()) // ID temporaire pour exclure
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new IllegalArgumentException(
                                            "A POI with this name already exists in the organization"));
                                }
                                return Mono.just(validatedDto);
                            });
                })
                .map(mapper::toEntity)
                .doOnNext(entity -> {
                    // Définir les timestamps
                    Instant now = Instant.now();
                    entity.setCreatedAt(now);
                    entity.setUpdatedAt(now);

                    // Définir les valeurs par défaut
                    if (entity.getIsActive() == null) {
                        entity.setIsActive(true);
                    }
                    if (entity.getPopularityScore() == null) {
                        entity.setPopularityScore(0.0f);
                    }
                })
                .flatMap(repository::save)
                .map(mapper::toDto)
                .doOnSuccess(savedDto -> log.info("POI created successfully with ID: {}", savedDto.getPoiId()))
                .doOnError(error -> log.error("Error creating POI: {}", error.getMessage()));
    }

    /**
     * Met à jour un POI existant
     */
    @Transactional
    public Mono<PointOfInterestDTO> updatePoi(UUID poiId, PointOfInterestDTO dto) {
        return Mono.fromCallable(() -> {
            log.info("Updating POI with ID: {}", poiId);

            if (poiId == null) {
                throw new IllegalArgumentException("POI ID cannot be null");
            }

            return dto;
        })
                .flatMap(validatedDto -> repository.findById(poiId))
                .switchIfEmpty(Mono.error(new RuntimeException("POI not found with ID: " + poiId)))
                .flatMap(existingEntity -> {
                    // Vérification de l'unicité du nom si le nom est modifié
                    if (dto.getPoiName() != null && !dto.getPoiName().equals(existingEntity.getPoiName())) {
                        return repository.existsByNameAndOrganizationIdExcludingId(
                                dto.getPoiName(),
                                existingEntity.getOrganizationId(),
                                poiId)
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new IllegalArgumentException(
                                                "A POI with this name already exists in the organization"));
                                    }
                                    return Mono.just(existingEntity);
                                });
                    }
                    return Mono.just(existingEntity);
                })
                .map(existingEntity -> {
                    // Mise à jour de l'entité avec les nouvelles données
                    PointOfInterest updatedEntity = mapper.updateEntityFromDto(existingEntity, dto);
                    updatedEntity.setUpdatedAt(Instant.now());
                    return updatedEntity;
                })
                .flatMap(repository::save)
                .map(mapper::toDto)
                .doOnSuccess(updatedDto -> log.info("POI updated successfully: {}", updatedDto.getPoiId()))
                .doOnError(error -> log.error("Error updating POI {}: {}", poiId, error.getMessage()));
    }

    /**
     * Trouve un POI par ID
     */
    public Mono<PointOfInterestDTO> findById(UUID poiId) {
        return repository.findById(poiId)
                .map(mapper::toDto)
                .doOnSuccess(dto -> log.debug("Found POI: {}", dto != null ? dto.getPoiId() : "null"))
                .doOnError(error -> log.error("Error finding POI {}: {}", poiId, error.getMessage()));
    }

    /**
     * Trouve tous les POIs actifs d'une organisation
     */
    public Flux<PointOfInterestDTO> findActiveByOrganizationId(UUID organizationId) {
        return repository.findActiveByOrganizationId(organizationId)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved active POIs for organization: {}", organizationId))
                .doOnError(error -> log.error("Error retrieving POIs for organization {}: {}",
                        organizationId, error.getMessage()));
    }

    /**
     * Trouve tous les POIs d'une organisation
     */
    public Flux<PointOfInterestDTO> findByOrganizationId(UUID organizationId) {
        return repository.findByOrganizationId(organizationId)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved all POIs for organization: {}", organizationId))
                .doOnError(error -> log.error("Error retrieving all POIs for organization {}: {}",
                        organizationId, error.getMessage()));
    }

    /**
     * Recherche de POIs avec filtres
     */
    public Flux<PointOfInterestDTO> searchWithFilters(UUID organizationId, String poiType,
            String poiCategory, String city, String searchTerm) {
        return repository.findWithFilters(organizationId, poiType, poiCategory, city, searchTerm)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Search completed with filters"))
                .doOnError(error -> log.error("Error in search with filters: {}", error.getMessage()));
    }

    /**
     * Trouve les POIs dans un rayon géographique
     */
    public Flux<PointOfInterestDTO> findByLocationWithinRadius(BigDecimal latitude, BigDecimal longitude,
            Double radiusKm) {
        return repository.findByLocationWithinRadius(latitude, longitude, radiusKm)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Location search completed"))
                .doOnError(error -> log.error("Error in location search: {}", error.getMessage()));
    }

    /**
     * Trouve les POIs par type
     */
    public Flux<PointOfInterestDTO> findByType(String poiType) {
        return repository.findByPoiType(poiType)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved POIs by type: {}", poiType))
                .doOnError(error -> log.error("Error retrieving POIs by type {}: {}", poiType, error.getMessage()));
    }

    /**
     * Trouve les POIs par catégorie
     */
    public Flux<PointOfInterestDTO> findByCategory(String poiCategory) {
        return repository.findByPoiCategory(poiCategory)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved POIs by category: {}", poiCategory))
                .doOnError(error -> log.error("Error retrieving POIs by category {}: {}",
                        poiCategory, error.getMessage()));
    }

    /**
     * Recherche par nom
     */
    public Flux<PointOfInterestDTO> searchByName(String name) {
        return repository.findByPoiNameContainingIgnoreCase(name)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Name search completed for: {}", name))
                .doOnError(error -> log.error("Error in name search for {}: {}", name, error.getMessage()));
    }

    /**
     * Trouve les POIs les plus populaires
     */
    public Flux<PointOfInterestDTO> findTopPopular(Integer limit) {
        return repository.findTopByPopularityScore(limit)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved top {} popular POIs", limit))
                .doOnError(error -> log.error("Error retrieving top popular POIs: {}", error.getMessage()));
    }

    /**
     * Désactive un POI
     */
    @Transactional
    public Mono<Void> deactivatePoi(UUID poiId) {
        return repository.deactivateById(poiId)
                .defaultIfEmpty(0) // Convertit null en 0
                .doOnSuccess(count -> {
                    if (count > 0) {
                        log.info("POI {} deactivated successfully", poiId);
                    } else {
                        log.warn("No POI found with ID {} to deactivate", poiId);
                    }
                })
                .doOnError(error -> log.error("Error deactivating POI {}: {}", poiId, error.getMessage()))
                .then();
    }

    /**
     * Réactive un POI
     */
    @Transactional
    public Mono<Void> activatePoi(UUID poiId) {
        return repository.activateById(poiId)
                .defaultIfEmpty(0) // Convertit null en 0
                .doOnSuccess(count -> {
                    if (count > 0) {
                        log.info("POI {} activated successfully", poiId);
                    } else {
                        log.warn("No POI found with ID {} to activate", poiId);
                    }
                })
                .doOnError(error -> log.error("Error activating POI {}: {}", poiId, error.getMessage()))
                .then();
    }

    /**
     * Supprime définitivement un POI
     */
    @Transactional
    public Mono<Void> deletePoi(UUID poiId) {
        return repository.findById(poiId)
                .switchIfEmpty(Mono.error(new RuntimeException("POI not found with ID: " + poiId)))
                .flatMap(poi -> repository.deleteById(poiId))
                .doOnSuccess(unused -> log.info("POI {} deleted successfully", poiId))
                .doOnError(error -> log.error("Error deleting POI {}: {}", poiId, error.getMessage()));
    }

    /**
     * Met à jour le score de popularité
     */
    @Transactional
    public Mono<Void> updatePopularityScore(UUID poiId, Float score) {
        return repository.updatePopularityScore(poiId, score)
                .defaultIfEmpty(0) // Convertit null en 0
                .doOnSuccess(count -> {
                    if (count > 0) {
                        log.info("Popularity score updated for POI {}: {}", poiId, score);
                    } else {
                        log.warn("No POI found with ID {} to update popularity score", poiId);
                    }
                })
                .doOnError(error -> log.error("Error updating popularity score for POI {}: {}",
                        poiId, error.getMessage()))
                .then();
    }

    /**
     * Compte les POIs actifs d'une organisation
     */
    public Mono<Long> countActiveByOrganizationId(UUID organizationId) {
        return repository.countActiveByOrganizationId(organizationId)
                .doOnSuccess(count -> log.debug("Active POI count for organization {}: {}",
                        organizationId, count))
                .doOnError(error -> log.error("Error counting POIs for organization {}: {}",
                        organizationId, error.getMessage()));
    }

    /**
     * Trouve les POIs créés par un utilisateur
     */
    public Flux<PointOfInterestDTO> findByCreatedByUserId(UUID userId) {
        return repository.findByCreatedByUserId(userId)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved POIs created by user: {}", userId))
                .doOnError(error -> log.error("Error retrieving POIs for user {}: {}",
                        userId, error.getMessage()));
    }

    /**
     * Trouve les POIs par ville
     */
    public Flux<PointOfInterestDTO> findByCity(String city) {
        return repository.findByCity(city)
                .map(mapper::toDto)
                .doOnComplete(() -> log.debug("Retrieved POIs for city: {}", city))
                .doOnError(error -> log.error("Error retrieving POIs for city {}: {}",
                        city, error.getMessage()));
    }

    /**
     * Vérifie l'existence d'un POI par nom et organisation
     */
    public Mono<Boolean> existsByNameAndOrganization(String name, UUID organizationId, UUID excludeId) {
        UUID excludeIdToUse = excludeId != null ? excludeId : UUID.randomUUID();
        return repository.existsByNameAndOrganizationIdExcludingId(name, organizationId, excludeIdToUse)
                .doOnSuccess(exists -> log.debug("POI name '{}' exists in organization {}: {}",
                        name, organizationId, exists))
                .doOnError(error -> log.error("Error checking POI name existence: {}", error.getMessage()));
    }

    /**
     * Met à jour les coordonnées GPS d'un POI
     */

    /*
     * @Transactional
     * public Mono<Void> updateCoordinates(UUID poiId, BigDecimal latitude,
     * BigDecimal longitude) {
     * return repository.updateCoordinates(poiId, latitude, longitude)
     * .doOnSuccess(updatedPoi -> {
     * if (updatedPoi != null) {
     * log.info("Coordinates updated for POI {}: ({}, {})", poiId, latitude,
     * longitude);
     * } else {
     * log.warn("No POI found with ID {} to update coordinates", poiId);
     * }
     * })
     * .doOnError(error -> log.error("Error updating coordinates for POI {}: {}",
     * poiId, error.getMessage()))
     * .then();
     * }
     * 
     */

}
