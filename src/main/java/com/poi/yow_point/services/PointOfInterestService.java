package com.poi.yow_point.services;

import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.mappers.PointOfInterestMapper;
//import com.poi.yow_point.models.PointOfInterest;
import com.poi.yow_point.repositories.PointOfInterestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class PointOfInterestService {

    private final PointOfInterestRepository repository;
    private final PointOfInterestMapper mapper;

    public PointOfInterestService(PointOfInterestRepository repository, PointOfInterestMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // CRUD de base
    public Mono<PointOfInterestDTO> create(PointOfInterestDTO dto) {
        return mapper.toEntity(dto)
                .map(entity -> {
                    entity.setPoiId(UUID.randomUUID());
                    entity.setCreatedAt(Instant.now());
                    entity.setUpdatedAt(Instant.now());
                    if (entity.getIsActive() == null) {
                        entity.setIsActive(true);
                    }
                    return entity;
                })
                .flatMap(repository::save)
                .flatMap(mapper::toDto);
    }

    public Mono<PointOfInterestDTO> findById(UUID id) {
        return repository.findById(id)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> findAll() {
        return repository.findAll()
                .flatMap(mapper::toDto);
    }

    public Mono<PointOfInterestDTO> update(UUID id, PointOfInterestDTO dto) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("PointOfInterest not found with id: " + id)))
                .flatMap(existingEntity -> {
                    dto.setPoiId(id);
                    dto.setCreatedAt(existingEntity.getCreatedAt());
                    dto.setUpdatedAt(Instant.now());
                    return mapper.toEntity(dto);
                })
                .flatMap(repository::save)
                .flatMap(mapper::toDto);
    }

    public Mono<Void> deleteById(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("PointOfInterest not found with id: " + id)))
                .flatMap(entity -> repository.deleteById(id));
    }

    public Mono<Boolean> existsById(UUID id) {
        return repository.existsById(id);
    }

    // Méthodes de recherche
    public Flux<PointOfInterestDTO> findByOrganizationId(UUID organizationId) {
        return repository.findByOrganizationId(organizationId)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> findActiveByOrganizationId(UUID organizationId) {
        return repository.findByOrganizationIdAndIsActive(organizationId, true)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> findByType(String poiType) {
        return repository.findByPoiType(poiType)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> findByCategory(String poiCategory) {
        return repository.findByPoiCategory(poiCategory)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> findByCity(String city) {
        return repository.findByAddressCity(city)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> searchByName(String name) {
        return repository.findByPoiNameContainingIgnoreCase(name)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> findByLocationWithinRadius(BigDecimal latitude, BigDecimal longitude,
            Double radiusKm) {
        return repository.findByLocationWithinRadius(latitude, longitude, radiusKm)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> findByMinPopularity(Float minScore) {
        return repository.findByPopularityScoreGreaterThanEqual(minScore)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> findByKeyword(String keyword) {
        return repository.findByKeywordContaining(keyword)
                .flatMap(mapper::toDto);
    }

    public Flux<PointOfInterestDTO> findTopPopular(Integer limit) {
        return repository.findTopByPopularity(limit)
                .flatMap(mapper::toDto);
    }

    // Méthodes de recherche avancée
    public Flux<PointOfInterestDTO> findByOrganizationAndTypeAndStatus(UUID organizationId, String poiType,
            Boolean isActive) {
        return repository.findByOrganizationAndTypeAndStatus(organizationId, poiType, isActive)
                .flatMap(mapper::toDto);
    }

    // Méthodes de comptage
    public Mono<Long> countByOrganizationId(UUID organizationId) {
        return repository.countByOrganizationId(organizationId);
    }

    public Mono<Long> countActive() {
        return repository.countByIsActive(true);
    }

    public Mono<Long> countInactive() {
        return repository.countByIsActive(false);
    }

    // Méthodes utilitaires
    public Mono<PointOfInterestDTO> activate(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("PointOfInterest not found with id: " + id)))
                .map(entity -> {
                    entity.setIsActive(true);
                    entity.setUpdatedAt(Instant.now());
                    return entity;
                })
                .flatMap(repository::save)
                .flatMap(mapper::toDto);
    }

    public Mono<PointOfInterestDTO> deactivate(UUID id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("PointOfInterest not found with id: " + id)))
                .map(entity -> {
                    entity.setIsActive(false);
                    entity.setUpdatedAt(Instant.now());
                    return entity;
                })
                .flatMap(repository::save)
                .flatMap(mapper::toDto);
    }

    public Mono<PointOfInterestDTO> updatePopularityScore(UUID id, Float newScore) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("PointOfInterest not found with id: " + id)))
                .map(entity -> {
                    entity.setPopularityScore(newScore);
                    entity.setUpdatedAt(Instant.now());
                    return entity;
                })
                .flatMap(repository::save)
                .flatMap(mapper::toDto);
    }
}