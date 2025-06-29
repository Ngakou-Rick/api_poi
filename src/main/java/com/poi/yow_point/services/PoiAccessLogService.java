package com.poi.yow_point.services;

import com.poi.yow_point.dto.PoiAccessLogDTO;
import com.poi.yow_point.mappers.PoiAccessLogMapper;
//import com.poi.yow_point.models.PoiAccessLog;
import com.poi.yow_point.repositories.PoiAccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoiAccessLogService {

    private final PoiAccessLogRepository repository;
    private final PoiAccessLogMapper mapper;

    @Autowired
    private final DatabaseClient databaseClient;

    /**
     * Crée un nouveau log d'accès
     */
    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Transactional
    public Mono<PoiAccessLogDTO> createAccessLog(PoiAccessLogDTO dto) {
        log.debug("Création d'un nouveau log d'accès pour POI: {}", dto.getPoiId());

        return mapper.toEntity(dto)
                .flatMap(entity -> {
                    // Générer un ID si non fourni
                    if (entity.getAccessId() == null) {
                        entity.setAccessId(UUID.randomUUID());
                    }
                    // S'assurer que la date d'accès est définie
                    if (entity.getAccessDatetime() == null) {
                        entity.setAccessDatetime(OffsetDateTime.now());
                    }

                    // Utiliser R2dbcEntityTemplate au lieu du repository
                    return entityTemplate.insert(entity);
                })
                .flatMap(mapper::toDTO)
                .doOnSuccess(result -> log.info("Log d'accès créé avec succès: {}", result.getAccessId()))
                .doOnError(error -> log.error("Erreur lors de la création du log d'accès: {}", error.getMessage()));
    }

    /**
     * Récupère un log d'accès par ID
     */
    public Mono<PoiAccessLogDTO> getAccessLogById(UUID accessId) {
        log.debug("Recherche du log d'accès: {}", accessId);

        return repository.findById(accessId)
                .flatMap(mapper::toDTO)
                .doOnNext(result -> log.debug("Log d'accès trouvé: {}", accessId))
                .switchIfEmpty(Mono.error(new RuntimeException("Log d'accès non trouvé: " + accessId)));
    }

    /**
     * Récupère tous les logs d'accès pour un POI
     */
    public Flux<PoiAccessLogDTO> getAccessLogsByPoiId(UUID poiId) {
        log.debug("Recherche des logs d'accès pour POI: {}", poiId);

        return repository.findByPoiId(poiId)
                .flatMap(mapper::toDTO)
                .doOnComplete(() -> log.debug("Récupération terminée pour POI: {}", poiId));
    }

    /**
     * Récupère les logs d'accès par organisation
     */
    public Flux<PoiAccessLogDTO> getAccessLogsByOrganizationId(UUID organizationId) {
        log.debug("Recherche des logs d'accès pour organisation: {}", organizationId);

        return repository.findByOrganizationId(organizationId)
                .flatMap(mapper::toDTO);
    }

    /**
     * Récupère les logs d'accès par utilisateur
     */
    public Flux<PoiAccessLogDTO> getAccessLogsByUserId(UUID userId) {
        log.debug("Recherche des logs d'accès pour utilisateur: {}", userId);

        return repository.findByUserId(userId)
                .flatMap(mapper::toDTO);
    }

    /**
     * Récupère les logs d'accès par type d'accès
     */
    public Flux<PoiAccessLogDTO> getAccessLogsByAccessType(String accessType) {
        log.debug("Recherche des logs d'accès pour type: {}", accessType);

        return repository.findByAccessType(accessType)
                .flatMap(mapper::toDTO);
    }

    /**
     * Récupère les logs d'accès par plateforme
     */
    public Flux<PoiAccessLogDTO> getAccessLogsByPlatformType(String platformType) {
        log.debug("Recherche des logs d'accès pour plateforme: {}", platformType);

        return repository.findByPlatformType(platformType)
                .flatMap(mapper::toDTO);
    }

    /**
     * Récupère les logs d'accès pour un POI et une organisation
     */
    public Flux<PoiAccessLogDTO> getAccessLogsByPoiAndOrganization(UUID poiId, UUID organizationId) {
        log.debug("Recherche des logs d'accès pour POI: {} et organisation: {}", poiId, organizationId);

        return repository.findByPoiIdAndOrganizationId(poiId, organizationId)
                .flatMap(mapper::toDTO);
    }

    /**
     * Récupère les logs d'accès par période
     */
    public Flux<PoiAccessLogDTO> getAccessLogsByDateRange(OffsetDateTime startDate, OffsetDateTime endDate) {
        log.debug("Recherche des logs d'accès entre {} et {}", startDate, endDate);

        return repository.findByAccessDatetimeBetween(startDate, endDate)
                .flatMap(mapper::toDTO);
    }

    /**
     * Récupère les logs d'accès récents pour un POI
     */
    public Flux<PoiAccessLogDTO> getRecentAccessLogsByPoiId(UUID poiId, OffsetDateTime since) {
        log.debug("Recherche des logs d'accès récents pour POI: {} depuis {}", poiId, since);

        return repository.findRecentByPoiId(poiId, since)
                .flatMap(mapper::toDTO);
    }

    /**
     * Récupère les logs d'accès avec pagination
     */
    public Flux<PoiAccessLogDTO> getAccessLogsByPoiIdWithPagination(UUID poiId, int page, int size) {
        int offset = page * size;
        log.debug("Recherche paginée des logs d'accès pour POI: {} (page: {}, taille: {})", poiId, page, size);

        return repository.findByPoiIdWithPagination(poiId, size, offset)
                .flatMap(mapper::toDTO);
    }

    /**
     * Compte les accès pour un POI
     */
    public Mono<Long> countAccessLogsByPoiId(UUID poiId) {
        log.debug("Comptage des accès pour POI: {}", poiId);

        return repository.countByPoiId(poiId);
    }

    /**
     * Compte les accès par type pour un POI
     */
    public Mono<Long> countAccessLogsByPoiIdAndAccessType(UUID poiId, String accessType) {
        log.debug("Comptage des accès de type {} pour POI: {}", accessType, poiId);

        return repository.countByPoiIdAndAccessType(poiId, accessType);
    }

    /**
     * Récupère les statistiques par plateforme pour une organisation
     */
    public Flux<Map<String, Object>> getPlatformStatsForOrganization(UUID organizationId) {
        return databaseClient.sql("""
                    SELECT platform_type, COUNT(*) AS count
                    FROM poi_access_log
                    WHERE organization_id = :organizationId
                    GROUP BY platform_type
                """)
                .bind("organizationId", organizationId)
                .map((row, metadata) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("platformType", row.get("platform_type", String.class));
                    map.put("count", row.get("count", Long.class));
                    return map;
                })
                .all();
    }

    /**
     * Met à jour un log d'accès
     */
    @Transactional
    public Mono<PoiAccessLogDTO> updateAccessLog(UUID accessId, PoiAccessLogDTO dto) {
        log.debug("Mise à jour du log d'accès: {}", accessId);

        return repository.findById(accessId)
                .switchIfEmpty(Mono.error(new RuntimeException("Log d'accès non trouvé: " + accessId)))
                .flatMap(existingEntity -> mapper.updateEntityFromDTO(existingEntity, dto))
                .flatMap(repository::save)
                .flatMap(mapper::toDTO)
                .doOnSuccess(result -> log.info("Log d'accès mis à jour: {}", accessId))
                .doOnError(error -> log.error("Erreur lors de la mise à jour du log d'accès {}: {}", accessId,
                        error.getMessage()));
    }

    /**
     * Supprime un log d'accès
     */
    @Transactional
    public Mono<Void> deleteAccessLog(UUID accessId) {
        log.debug("Suppression du log d'accès: {}", accessId);

        return repository.findById(accessId)
                .switchIfEmpty(Mono.error(new RuntimeException("Log d'accès non trouvé: " + accessId)))
                .flatMap(entity -> repository.delete(entity))
                .doOnSuccess(result -> log.info("Log d'accès supprimé: {}", accessId))
                .doOnError(error -> log.error("Erreur lors de la suppression du log d'accès {}: {}", accessId,
                        error.getMessage()));
    }

    /**
     * Supprime les logs anciens
     */
    @Transactional
    public Mono<Long> deleteOldLogs(OffsetDateTime beforeDate) {
        log.info("Suppression des logs d'accès antérieurs à: {}", beforeDate);

        return repository.deleteOldLogs(beforeDate)
                .doOnSuccess(count -> log.info("Nombre de logs supprimés: {}", count))
                .doOnError(
                        error -> log.error("Erreur lors de la suppression des anciens logs: {}", error.getMessage()));
    }

    /**
     * Récupère tous les logs d'accès
     */
    public Flux<PoiAccessLogDTO> getAllAccessLogs() {
        log.debug("Récupération de tous les logs d'accès");

        return repository.findAll()
                .flatMap(mapper::toDTO);
    }
}