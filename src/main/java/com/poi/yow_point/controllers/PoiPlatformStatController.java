package com.poi.yow_point.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.poi.yow_point.dto.PoiPlatformStatDTO;
import com.poi.yow_point.services.PoiPlatformStatService;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/poi-platform-stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PoiPlatformStatController {

    private final PoiPlatformStatService service;

    /**
     * Créer une nouvelle statistique
     */
    @PostMapping
    public Mono<ResponseEntity<PoiPlatformStatDTO>> createStat(@Valid @RequestBody PoiPlatformStatDTO statDTO) {
        return service.createStat(statDTO)
                .map(createdStat -> ResponseEntity.status(HttpStatus.CREATED).body(createdStat))
                .doOnSuccess(response -> log.info("Statistique créée avec succès"))
                .onErrorResume(error -> {
                    log.error("Erreur lors de la création de la statistique", error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    /**
     * Récupérer toutes les statistiques
     */
    @GetMapping
    public Flux<PoiPlatformStatDTO> getAllStats() {
        return service.getAllStats()
                .doOnComplete(() -> log.info("Récupération de toutes les statistiques terminée"));
    }

    /**
     * Récupérer une statistique par ID
     */
    @GetMapping("/{statId}")
    public Mono<ResponseEntity<PoiPlatformStatDTO>> getStatById(@PathVariable UUID statId) {
        return service.getStatById(statId)
                .map(stat -> ResponseEntity.ok(stat))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> log.info("Recherche de statistique par ID: {} - Status: {}",
                        statId, response.getStatusCode()));
    }

    /**
     * Récupérer les statistiques par organisation
     */
    @GetMapping("/organization/{orgId}")
    public Flux<PoiPlatformStatDTO> getStatsByOrgId(@PathVariable UUID orgId) {
        return service.getStatsByOrgId(orgId)
                .doOnComplete(() -> log.info("Récupération des statistiques pour l'organisation: {}", orgId));
    }

    /**
     * Récupérer les statistiques par point d'intérêt
     */
    @GetMapping("/poi/{poiId}")
    public Flux<PoiPlatformStatDTO> getStatsByPoiId(@PathVariable UUID poiId) {
        return service.getStatsByPoiId(poiId)
                .doOnComplete(() -> log.info("Récupération des statistiques pour le POI: {}", poiId));
    }

    /**
     * Récupérer les statistiques par type de plateforme
     */
    @GetMapping("/platform/{platformType}")
    public Flux<PoiPlatformStatDTO> getStatsByPlatformType(@PathVariable String platformType) {
        return service.getStatsByPlatformType(platformType)
                .doOnComplete(() -> log.info("Récupération des statistiques pour la plateforme: {}", platformType));
    }

    /**
     * Récupérer les statistiques par date
     */
    @GetMapping("/date/{date}")
    public Flux<PoiPlatformStatDTO> getStatsByDate(@PathVariable LocalDate date) {
        return service.getStatsByDate(date)
                .doOnComplete(() -> log.info("Récupération des statistiques pour la date: {}", date));
    }

    /**
     * Récupérer les statistiques par plage de dates
     */
    @GetMapping("/date-range")
    public Flux<PoiPlatformStatDTO> getStatsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return service.getStatsByDateRange(startDate, endDate)
                .doOnComplete(() -> log.info("Récupération des statistiques entre {} et {}", startDate, endDate));
    }

    /**
     * Récupérer les statistiques par organisation et plage de dates
     */
    @GetMapping("/organization/{orgId}/date-range")
    public Flux<PoiPlatformStatDTO> getStatsByOrgIdAndDateRange(
            @PathVariable UUID orgId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return service.getStatsByOrgIdAndDateRange(orgId, startDate, endDate)
                .doOnComplete(() -> log.info("Récupération des statistiques pour l'organisation {} entre {} et {}",
                        orgId, startDate, endDate));
    }

    /**
     * Mettre à jour une statistique
     */
    @PutMapping("/{statId}")
    public Mono<ResponseEntity<PoiPlatformStatDTO>> updateStat(
            @PathVariable UUID statId,
            @Valid @RequestBody PoiPlatformStatDTO statDTO) {
        return service.updateStat(statId, statDTO)
                .map(updatedStat -> ResponseEntity.ok(updatedStat))
                .onErrorResume(error -> {
                    log.error("Erreur lors de la mise à jour de la statistique: {}", statId, error);
                    if (error.getMessage().contains("non trouvée")) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                })
                .doOnSuccess(response -> log.info("Mise à jour de la statistique: {} - Status: {}",
                        statId, response.getStatusCode()));
    }

    /**
     * Supprimer une statistique par ID
     */
    @DeleteMapping("/{statId}")
    public Mono<ResponseEntity<Void>> deleteStat(@PathVariable UUID statId) {
        return service.deleteStat(statId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(error -> {
                    log.error("Erreur lors de la suppression de la statistique: {}", statId, error);
                    if (error.getMessage().contains("non trouvée")) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                })
                .doOnSuccess(response -> log.info("Suppression de la statistique: {} - Status: {}",
                        statId, response.getStatusCode()));
    }

    /**
     * Supprimer toutes les statistiques d'une organisation
     */
    @DeleteMapping("/organization/{orgId}")
    public Mono<ResponseEntity<Void>> deleteStatsByOrgId(@PathVariable UUID orgId) {
        return service.deleteStatsByOrgId(orgId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(error -> {
                    log.error("Erreur lors de la suppression des statistiques pour l'organisation: {}", orgId, error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                })
                .doOnSuccess(response -> log.info("Suppression des statistiques pour l'organisation: {} - Status: {}",
                        orgId, response.getStatusCode()));
    }

    /**
     * Supprimer toutes les statistiques d'un point d'intérêt
     */
    @DeleteMapping("/poi/{poiId}")
    public Mono<ResponseEntity<Void>> deleteStatsByPoiId(@PathVariable UUID poiId) {
        return service.deleteStatsByPoiId(poiId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(error -> {
                    log.error("Erreur lors de la suppression des statistiques pour le POI: {}", poiId, error);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                })
                .doOnSuccess(response -> log.info("Suppression des statistiques pour le POI: {} - Status: {}",
                        poiId, response.getStatusCode()));
    }

    /**
     * Vérifier si une statistique existe
     */
    @GetMapping("/{statId}/exists")
    public Mono<ResponseEntity<Boolean>> existsById(@PathVariable UUID statId) {
        return service.existsById(statId)
                .map(exists -> ResponseEntity.ok(exists))
                .doOnSuccess(response -> log.debug("Vérification d'existence pour la statistique: {}", statId));
    }

    /**
     * Compter le nombre total de statistiques
     */
    @GetMapping("/count")
    public Mono<ResponseEntity<Long>> countAll() {
        return service.countAll()
                .map(count -> ResponseEntity.ok(count))
                .doOnSuccess(response -> log.debug("Comptage total des statistiques: {}", response.getBody()));
    }
}