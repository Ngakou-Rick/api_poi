package com.poi.yow_point.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.poi.yow_point.dto.PoiPlatformStatDTO;
import com.poi.yow_point.mappers.PoiPlatformStatMapper;
import com.poi.yow_point.models.PoiPlatformStat;
import com.poi.yow_point.repositories.PoiPlatformStatRepository;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoiPlatformStatService {

    private final PoiPlatformStatRepository repository;
    private final PoiPlatformStatMapper mapper;

    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    /**
     * Créer une nouvelle statistique
     */
    public Mono<PoiPlatformStatDTO> createStat(PoiPlatformStatDTO statDTO) {
        return Mono.just(statDTO)
                .map(dto -> {
                    // Création de l'entité avec valeurs par défaut
                    PoiPlatformStat entity = mapper.toEntity(dto);
                    if (entity.getStatId() == null) {
                        entity.setStatId(UUID.randomUUID());
                    }
                    if (entity.getStatDate() == null) {
                        entity.setStatDate(LocalDate.now());
                    }
                    return entity;
                })
                .flatMap(entity -> entityTemplate.insert(PoiPlatformStat.class)
                        .using(entity)
                        .map(mapper::toDTO))
                .doOnSuccess(dto -> log.info("Statistique créée avec l'ID: {}", dto.getStatId()))
                .doOnError(error -> log.error("Erreur lors de la création de la statistique", error));
    }

    /**
     * Récupérer toutes les statistiques
     */
    public Flux<PoiPlatformStatDTO> getAllStats() {
        return repository.findAll()
                .map(mapper::toDTO)
                .doOnComplete(() -> log.info("Récupération de toutes les statistiques terminée"));
    }

    /**
     * Récupérer une statistique par ID
     */
    public Mono<PoiPlatformStatDTO> getStatById(UUID statId) {
        return repository.findById(statId)
                .map(mapper::toDTO)
                .doOnSuccess(dto -> log.info("Statistique trouvée: {}", statId))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Statistique non trouvée avec l'ID: {}", statId);
                    return Mono.empty();
                }));
    }

    /**
     * Récupérer les statistiques par organisation
     */
    public Flux<PoiPlatformStatDTO> getStatsByOrgId(UUID orgId) {
        return repository.findByOrgId(orgId)
                .map(mapper::toDTO)
                .doOnComplete(() -> log.info("Récupération des statistiques pour l'organisation: {}", orgId));
    }

    /**
     * Récupérer les statistiques par point d'intérêt
     */
    public Flux<PoiPlatformStatDTO> getStatsByPoiId(UUID poiId) {
        return repository.findByPoiId(poiId)
                .map(mapper::toDTO)
                .doOnComplete(() -> log.info("Récupération des statistiques pour le POI: {}", poiId));
    }

    /**
     * Récupérer les statistiques par type de plateforme
     */
    public Flux<PoiPlatformStatDTO> getStatsByPlatformType(String platformType) {
        return repository.findByPlatformType(platformType)
                .map(mapper::toDTO)
                .doOnComplete(() -> log.info("Récupération des statistiques pour la plateforme: {}", platformType));
    }

    /**
     * Récupérer les statistiques par date
     */
    public Flux<PoiPlatformStatDTO> getStatsByDate(LocalDate statDate) {
        return repository.findByStatDate(statDate)
                .map(mapper::toDTO)
                .doOnComplete(() -> log.info("Récupération des statistiques pour la date: {}", statDate));
    }

    /**
     * Récupérer les statistiques par plage de dates
     */
    public Flux<PoiPlatformStatDTO> getStatsByDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.findByStatDateBetween(startDate, endDate)
                .map(mapper::toDTO)
                .doOnComplete(() -> log.info("Récupération des statistiques entre {} et {}", startDate, endDate));
    }

    /**
     * Récupérer les statistiques par organisation et plage de dates
     */
    public Flux<PoiPlatformStatDTO> getStatsByOrgIdAndDateRange(UUID orgId, LocalDate startDate, LocalDate endDate) {
        return repository.findByOrgIdAndDateRange(orgId, startDate, endDate)
                .map(mapper::toDTO)
                .doOnComplete(() -> log.info("Récupération des statistiques pour l'organisation {} entre {} et {}",
                        orgId, startDate, endDate));
    }

    /**
     * Mettre à jour une statistique
     */
    public Mono<PoiPlatformStatDTO> updateStat(UUID statId, PoiPlatformStatDTO statDTO) {
        return repository.findById(statId)
                .switchIfEmpty(Mono.error(new RuntimeException("Statistique non trouvée avec l'ID: " + statId)))
                .map(existingStat -> {
                    PoiPlatformStat updatedStat = mapper.toEntity(statDTO);
                    updatedStat.setStatId(statId); // Conserve l'ID original
                    return updatedStat;
                })
                .flatMap(repository::save)
                .map(mapper::toDTO)
                .doOnSuccess(dto -> log.info("Statistique mise à jour: {}", statId))
                .doOnError(error -> log.error("Erreur lors de la mise à jour de la statistique: {}", statId, error));
    }

    /**
     * Supprimer une statistique par ID
     */
    public Mono<Void> deleteStat(UUID statId) {
        return repository.findById(statId)
                .switchIfEmpty(Mono.error(new RuntimeException("Statistique non trouvée avec l'ID: " + statId)))
                .flatMap(stat -> repository.deleteById(statId))
                .doOnSuccess(v -> log.info("Statistique supprimée: {}", statId))
                .doOnError(error -> log.error("Erreur lors de la suppression de la statistique: {}", statId, error));
    }

    /**
     * Supprimer toutes les statistiques d'une organisation
     */
    public Mono<Void> deleteStatsByOrgId(UUID orgId) {
        return repository.deleteByOrgId(orgId)
                .doOnSuccess(v -> log.info("Statistiques supprimées pour l'organisation: {}", orgId))
                .doOnError(error -> log.error("Erreur lors de la suppression des statistiques pour l'organisation: {}",
                        orgId, error));
    }

    /**
     * Supprimer toutes les statistiques d'un point d'intérêt
     */
    public Mono<Void> deleteStatsByPoiId(UUID poiId) {
        return repository.deleteByPoiId(poiId)
                .doOnSuccess(v -> log.info("Statistiques supprimées pour le POI: {}", poiId))
                .doOnError(error -> log.error("Erreur lors de la suppression des statistiques pour le POI: {}", poiId,
                        error));
    }

    /**
     * Vérifier si une statistique existe
     */
    public Mono<Boolean> existsById(UUID statId) {
        return repository.existsById(statId)
                .doOnNext(exists -> log.debug("Statistique {} existe: {}", statId, exists));
    }

    /**
     * Compter le nombre total de statistiques
     */
    public Mono<Long> countAll() {
        return repository.count()
                .doOnNext(count -> log.debug("Nombre total de statistiques: {}", count));
    }
}