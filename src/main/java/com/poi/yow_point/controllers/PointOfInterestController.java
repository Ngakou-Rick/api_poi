package com.poi.yow_point.controllers;

import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.services.PointOfInterestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
//import org.springframework.validation.annotation.Validated;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pois")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class PointOfInterestController {

    private final PointOfInterestService poiService;

    /**
     * Crée un nouveau POI
     */
    @PostMapping
    public Mono<ResponseEntity<PointOfInterestDTO>> createPoi(@Valid @RequestBody PointOfInterestDTO dto) {
        log.info("REST request to create POI: {}", dto.getPoiName());

        return poiService.createPoi(dto)
                .map(savedDto -> ResponseEntity.status(HttpStatus.CREATED).body(savedDto))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error creating POI", ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    /**
     * Met à jour un POI existant
     */
    @PutMapping("/{poiId}")
    public Mono<ResponseEntity<PointOfInterestDTO>> updatePoi(
            @PathVariable UUID poiId,
            @Valid @RequestBody PointOfInterestDTO dto) {
        log.info("REST request to update POI: {}", poiId);

        return poiService.updatePoi(poiId, dto)
                .map(updatedDto -> ResponseEntity.ok(updatedDto))
                .onErrorResume(IllegalArgumentException.class,
                        ex -> Mono.just(ResponseEntity.badRequest().build()))
                .onErrorResume(RuntimeException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error updating POI: {}", poiId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    /**
     * Récupère un POI par ID
     */
    @GetMapping("/{poiId}")
    public Mono<ResponseEntity<PointOfInterestDTO>> getPoiById(@PathVariable UUID poiId) {
        log.debug("REST request to get POI: {}", poiId);

        return poiService.findById(poiId)
                .map(dto -> ResponseEntity.ok(dto))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POI: {}", poiId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    /**
     * Récupère tous les POIs actifs d'une organisation
     */
    @GetMapping("/organization/{organizationId}")
    public Flux<PointOfInterestDTO> getPoisByOrganization(@PathVariable UUID organizationId) {
        log.debug("REST request to get POIs for organization: {}", organizationId);

        return poiService.findActiveByOrganizationId(organizationId)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs for organization: {}", organizationId, ex);
                            return Flux.empty();
                        });
    }

    /**
     * Récupère tous les POIs (actifs et inactifs) d'une organisation
     */
    @GetMapping("/organization/{organizationId}/all")
    public Flux<PointOfInterestDTO> getAllPoisByOrganization(@PathVariable UUID organizationId) {
        log.debug("REST request to get all POIs for organization: {}", organizationId);

        return poiService.findByOrganizationId(organizationId)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving all POIs for organization: {}", organizationId, ex);
                            return Flux.empty();
                        });
    }

    /**
     * Recherche de POIs avec filtres multiples
     */
    @GetMapping("/search")
    public Flux<PointOfInterestDTO> searchPois(
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String searchTerm) {
        log.debug("REST request to search POIs with filters");

        return poiService.searchWithFilters(organizationId, type, category, city, searchTerm)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error in POI search", ex);
                            return Flux.empty();
                        });
    }

    /**
     * Recherche géographique dans un rayon
     */
    @GetMapping("/location")
    public Flux<PointOfInterestDTO> getPoisByLocation(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(defaultValue = "10.0") Double radiusKm) {
        log.debug("REST request to get POIs by location: {}, {} within {} km",
                latitude, longitude, radiusKm);

        return poiService.findByLocationWithinRadius(latitude, longitude, radiusKm)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error in location-based POI search", ex);
                            return Flux.empty();
                        });
    }

    /**
     * Récupère les POIs par type
     */
    @GetMapping("/type/{type}")
    public Flux<PointOfInterestDTO> getPoisByType(@PathVariable String type) {
        log.debug("REST request to get POIs by type: {}", type);

        return poiService.findByType(type)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs by type: {}", type, ex);
                            return Flux.empty();
                        });
    }

    /**
     * Récupère les POIs par catégorie
     */
    @GetMapping("/category/{category}")
    public Flux<PointOfInterestDTO> getPoisByCategory(@PathVariable String category) {
        log.debug("REST request to get POIs by category: {}", category);

        return poiService.findByCategory(category)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs by category: {}", category, ex);
                            return Flux.empty();
                        });
    }

    /**
     * Recherche par nom
     */
    @GetMapping("/name/{name}")
    public Flux<PointOfInterestDTO> searchPoisByName(@PathVariable String name) {
        log.debug("REST request to search POIs by name: {}", name);

        return poiService.searchByName(name)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error searching POIs by name: {}", name, ex);
                            return Flux.empty();
                        });
    }

    /**
     * Récupère les POIs par ville
     */
    @GetMapping("/city/{city}")
    public Flux<PointOfInterestDTO> getPoisByCity(@PathVariable String city) {
        log.debug("REST request to get POIs by city: {}", city);

        return poiService.findByCity(city)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs by city: {}", city, ex);
                            return Flux.empty();
                        });
    }

    /**
     * Récupère les POIs les plus populaires
     */
    @GetMapping("/popular")
    public Flux<PointOfInterestDTO> getTopPopularPois(
            @RequestParam(defaultValue = "10") Integer limit) {
        log.debug("REST request to get top {} popular POIs", limit);

        return poiService.findTopPopular(limit)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving popular POIs", ex);
                            return Flux.empty();
                        });
    }

    /**
     * Récupère les POIs créés par un utilisateur
     */
    @GetMapping("/user/{userId}")
    public Flux<PointOfInterestDTO> getPoisByUser(@PathVariable UUID userId) {
        log.debug("REST request to get POIs created by user: {}", userId);

        return poiService.findByCreatedByUserId(userId)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs for user: {}", userId, ex);
                            return Flux.empty();
                        });
    }

    /**
     * Désactive un POI (soft delete)
     */
    @PatchMapping("/{poiId}/deactivate")
    public Mono<ResponseEntity<Void>> deactivatePoi(@PathVariable UUID poiId) {
        log.info("REST request to deactivate POI: {}", poiId);

        return poiService.deactivatePoi(poiId)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error deactivating POI: {}", poiId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    /**
     * Réactive un POI
     */
    @PatchMapping("/{poiId}/activate")
    public Mono<ResponseEntity<Void>> activatePoi(@PathVariable UUID poiId) {
        log.info("REST request to activate POI: {}", poiId);

        return poiService.activatePoi(poiId)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error activating POI: {}", poiId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    /**
     * Supprime définitivement un POI
     */
    @DeleteMapping("/{poiId}")
    public Mono<ResponseEntity<Void>> deletePoi(@PathVariable UUID poiId) {
        log.info("REST request to delete POI: {}", poiId);

        return poiService.deletePoi(poiId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorResume(RuntimeException.class,
                        ex -> Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error deleting POI: {}", poiId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    /**
     * Met à jour le score de popularité
     */
    @PatchMapping("/{poiId}/popularity")
    public Mono<ResponseEntity<Void>> updatePopularityScore(
            @PathVariable UUID poiId,
            @RequestParam Float score) {
        log.info("REST request to update popularity score for POI: {} to {}", poiId, score);

        if (score < 0 || score > 100) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return poiService.updatePopularityScore(poiId, score)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error updating popularity score for POI: {}", poiId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    /**
     * Compte les POIs actifs d'une organisation
     */
    @GetMapping("/organization/{organizationId}/count")
    public Mono<ResponseEntity<Long>> countActivePoisByOrganization(@PathVariable UUID organizationId) {
        log.debug("REST request to count active POIs for organization: {}", organizationId);

        return poiService.countActiveByOrganizationId(organizationId)
                .map(count -> ResponseEntity.ok(count))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error counting POIs for organization: {}", organizationId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    /**
     * Vérifie l'existence d'un POI par nom dans une organisation
     */
    @GetMapping("/check-name")
    public Mono<ResponseEntity<Boolean>> checkPoiNameExists(
            @RequestParam String name,
            @RequestParam UUID organizationId,
            @RequestParam(required = false) UUID excludeId) {
        log.debug("REST request to check POI name existence: {} in organization: {}", name, organizationId);

        return poiService.existsByNameAndOrganization(name, organizationId, excludeId)
                .map(exists -> ResponseEntity.ok(exists))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error checking POI name existence", ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }
}