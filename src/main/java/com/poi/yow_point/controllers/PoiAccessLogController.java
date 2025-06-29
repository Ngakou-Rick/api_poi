package com.poi.yow_point.controllers;

import com.poi.yow_point.dto.PoiAccessLogDTO;
import com.poi.yow_point.services.PoiAccessLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/poi-access-logs")
@RequiredArgsConstructor
public class PoiAccessLogController {

    private final PoiAccessLogService service;

    /**
     * Crée un nouveau log d'accès
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PoiAccessLogDTO> createAccessLog(@Valid @RequestBody PoiAccessLogDTO dto) {
        log.info("Création d'un nouveau log d'accès pour POI: {}", dto.getPoiId());
        return service.createAccessLog(dto);
    }

    /**
     * Récupère un log d'accès par ID
     */
    @GetMapping(value = "/{accessId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<PoiAccessLogDTO> getAccessLogById(@PathVariable UUID accessId) {
        log.info("Récupération du log d'accès: {}", accessId);
        return service.getAccessLogById(accessId);
    }

    /**
     * Récupère tous les logs d'accès
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<PoiAccessLogDTO> getAllAccessLogs() {
        log.info("Récupération de tous les logs d'accès");
        return service.getAllAccessLogs();
    }

    /**
     * Récupère les logs d'accès par POI
     */
    @GetMapping("/poi/{poiId}")
    public Flux<PoiAccessLogDTO> getAccessLogsByPoiId(@PathVariable UUID poiId) {
        log.info("Récupération des logs d'accès pour POI: {}", poiId);
        return service.getAccessLogsByPoiId(poiId);
    }

    /**
     * Récupère les logs d'accès par organisation
     */
    @GetMapping("/organization/{organizationId}")
    public Flux<PoiAccessLogDTO> getAccessLogsByOrganizationId(@PathVariable UUID organizationId) {
        log.info("Récupération des logs d'accès pour organisation: {}", organizationId);
        return service.getAccessLogsByOrganizationId(organizationId);
    }

    /**
     * Récupère les logs d'accès par utilisateur
     */
    @GetMapping("/user/{userId}")
    public Flux<PoiAccessLogDTO> getAccessLogsByUserId(@PathVariable UUID userId) {
        log.info("Récupération des logs d'accès pour utilisateur: {}", userId);
        return service.getAccessLogsByUserId(userId);
    }

    /**
     * Récupère les logs d'accès par type d'accès
     */
    @GetMapping("/access-type/{accessType}")
    public Flux<PoiAccessLogDTO> getAccessLogsByAccessType(@PathVariable String accessType) {
        log.info("Récupération des logs d'accès pour type: {}", accessType);
        return service.getAccessLogsByAccessType(accessType);
    }

    /**
     * Récupère les logs d'accès par plateforme
     */
    @GetMapping("/platform/{platformType}")
    public Flux<PoiAccessLogDTO> getAccessLogsByPlatformType(@PathVariable String platformType) {
        log.info("Récupération des logs d'accès pour plateforme: {}", platformType);
        return service.getAccessLogsByPlatformType(platformType);
    }

    /**
     * Récupère les logs d'accès pour un POI et une organisation
     */
    @GetMapping("/poi/{poiId}/organization/{organizationId}")
    public Flux<PoiAccessLogDTO> getAccessLogsByPoiAndOrganization(
            @PathVariable UUID poiId,
            @PathVariable UUID organizationId) {
        log.info("Récupération des logs d'accès pour POI: {} et organisation: {}", poiId, organizationId);
        return service.getAccessLogsByPoiAndOrganization(poiId, organizationId);
    }

    /**
     * Récupère les logs d'accès par période
     */
    @GetMapping("/date-range")
    public Flux<PoiAccessLogDTO> getAccessLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate) {
        log.info("Récupération des logs d'accès entre {} et {}", startDate, endDate);
        return service.getAccessLogsByDateRange(startDate, endDate);
    }

    /**
     * Récupère les logs d'accès récents pour un POI
     */
    @GetMapping("/poi/{poiId}/recent")
    public Flux<PoiAccessLogDTO> getRecentAccessLogsByPoiId(
            @PathVariable UUID poiId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime since) {
        log.info("Récupération des logs d'accès récents pour POI: {} depuis {}", poiId, since);
        return service.getRecentAccessLogsByPoiId(poiId, since);
    }

    /**
     * Récupère les logs d'accès avec pagination
     */
    @GetMapping("/poi/{poiId}/paginated")
    public Flux<PoiAccessLogDTO> getAccessLogsByPoiIdWithPagination(
            @PathVariable UUID poiId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Récupération paginée des logs d'accès pour POI: {} (page: {}, taille: {})", poiId, page, size);
        return service.getAccessLogsByPoiIdWithPagination(poiId, page, size);
    }

    /**
     * Compte les accès pour un POI
     */
    @GetMapping("/poi/{poiId}/count")
    public Mono<ResponseEntity<Long>> countAccessLogsByPoiId(@PathVariable UUID poiId) {
        log.info("Comptage des accès pour POI: {}", poiId);

        return service.countAccessLogsByPoiId(poiId)
                .map(count -> ResponseEntity.ok(count))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Compte les accès par type pour un POI
     */
    @GetMapping("/poi/{poiId}/count/access-type/{accessType}")
    public Mono<ResponseEntity<Long>> countAccessLogsByPoiIdAndAccessType(
            @PathVariable UUID poiId,
            @PathVariable String accessType) {
        log.info("Comptage des accès de type {} pour POI: {}", accessType, poiId);

        return service.countAccessLogsByPoiIdAndAccessType(poiId, accessType)
                .map(count -> ResponseEntity.ok(count))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Récupère les statistiques par plateforme pour une organisation
     */
    @GetMapping("/organization/{organizationId}/platform-stats")
    public Flux<Map<String, Object>> getPlatformStatsForOrganization(@PathVariable UUID organizationId) {
        log.info("Récupération des statistiques par plateforme pour organisation: {}", organizationId);
        return service.getPlatformStatsForOrganization(organizationId);
    }

    /**
     * Met à jour un log d'accès
     */
    @PutMapping("/{accessId}")
    public Mono<ResponseEntity<PoiAccessLogDTO>> updateAccessLog(
            @PathVariable UUID accessId,
            @Valid @RequestBody PoiAccessLogDTO dto) {
        log.info("Mise à jour du log d'accès: {}", accessId);

        return service.updateAccessLog(accessId, dto)
                .map(result -> ResponseEntity.ok(result))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    /**
     * Supprime un log d'accès
     */
    @DeleteMapping("/{accessId}")
    public Mono<ResponseEntity<Void>> deleteAccessLog(@PathVariable UUID accessId) {
        log.info("Suppression du log d'accès: {}", accessId);

        return service.deleteAccessLog(accessId)
                .map(result -> ResponseEntity.noContent().<Void>build())
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    /**
     * Supprime les logs anciens
     */
    @DeleteMapping("/cleanup")
    public Mono<ResponseEntity<Map<String, Object>>> deleteOldLogs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime beforeDate) {
        log.info("Suppression des logs d'accès antérieurs à: {}", beforeDate);

        return service.deleteOldLogs(beforeDate)
                .map(count -> {
                    Map<String, Object> response = Map.of(
                            "deletedCount", count,
                            "beforeDate", beforeDate);
                    return ResponseEntity.ok(response);
                })
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    /**
     * Endpoint de santé pour vérifier le service
     */
    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, String>>> health() {
        return Mono.just(ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "PoiAccessLogService",
                "timestamp", OffsetDateTime.now().toString())));
    }
}