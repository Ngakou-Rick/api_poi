package com.poi.yow_point.controllers;

import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.services.PointOfInterestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/points-of-interest")
@CrossOrigin(origins = "*")
public class PointOfInterestController {

    private final PointOfInterestService service;

    public PointOfInterestController(PointOfInterestService service) {
        this.service = service;
    }

    // CRUD de base
    @PostMapping
    public Mono<ResponseEntity<PointOfInterestDTO>> create(@Valid @RequestBody PointOfInterestDTO dto) {
        return service.create(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PointOfInterestDTO>> findById(@PathVariable UUID id) {
        return service.findById(id)
                .map(dto -> ResponseEntity.ok(dto))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<PointOfInterestDTO> findAll() {
        return service.findAll();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PointOfInterestDTO>> update(
            @PathVariable UUID id,
            @Valid @RequestBody PointOfInterestDTO dto) {
        return service.update(id, dto)
                .map(updated -> ResponseEntity.ok(updated))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable UUID id) {
        return service.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/exists")
    public Mono<ResponseEntity<Boolean>> existsById(@PathVariable UUID id) {
        return service.existsById(id)
                .map(exists -> ResponseEntity.ok(exists));
    }

    // Recherches par critères
    @GetMapping("/organization/{organizationId}")
    public Flux<PointOfInterestDTO> findByOrganizationId(@PathVariable UUID organizationId) {
        return service.findByOrganizationId(organizationId);
    }

    @GetMapping("/organization/{organizationId}/active")
    public Flux<PointOfInterestDTO> findActiveByOrganizationId(@PathVariable UUID organizationId) {
        return service.findActiveByOrganizationId(organizationId);
    }

    @GetMapping("/type/{poiType}")
    public Flux<PointOfInterestDTO> findByType(@PathVariable String poiType) {
        return service.findByType(poiType);
    }

    @GetMapping("/category/{poiCategory}")
    public Flux<PointOfInterestDTO> findByCategory(@PathVariable String poiCategory) {
        return service.findByCategory(poiCategory);
    }

    @GetMapping("/city/{city}")
    public Flux<PointOfInterestDTO> findByCity(@PathVariable String city) {
        return service.findByCity(city);
    }

    @GetMapping("/search")
    public Flux<PointOfInterestDTO> searchByName(@RequestParam String name) {
        return service.searchByName(name);
    }

    // Recherche géographique
    @GetMapping("/location/nearby")
    public Flux<PointOfInterestDTO> findNearby(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(defaultValue = "10.0") Double radiusKm) {
        return service.findByLocationWithinRadius(latitude, longitude, radiusKm);
    }

    // Recherche par popularité
    @GetMapping("/popular")
    public Flux<PointOfInterestDTO> findByMinPopularity(
            @RequestParam(defaultValue = "0.0") Float minScore) {
        return service.findByMinPopularity(minScore);
    }

    @GetMapping("/top-popular")
    public Flux<PointOfInterestDTO> findTopPopular(
            @RequestParam(defaultValue = "10") Integer limit) {
        return service.findTopPopular(limit);
    }

    // Recherche par mots-clés
    @GetMapping("/keyword")
    public Flux<PointOfInterestDTO> findByKeyword(@RequestParam String keyword) {
        return service.findByKeyword(keyword);
    }

    // Recherche avancée
    @GetMapping("/advanced")
    public Flux<PointOfInterestDTO> findByOrganizationAndTypeAndStatus(
            @RequestParam UUID organizationId,
            @RequestParam String poiType,
            @RequestParam(defaultValue = "true") Boolean isActive) {
        return service.findByOrganizationAndTypeAndStatus(organizationId, poiType, isActive);
    }

    // Statistiques
    @GetMapping("/organization/{organizationId}/count")
    public Mono<ResponseEntity<Long>> countByOrganizationId(@PathVariable UUID organizationId) {
        return service.countByOrganizationId(organizationId)
                .map(count -> ResponseEntity.ok(count));
    }

    @GetMapping("/count/active")
    public Mono<ResponseEntity<Long>> countActive() {
        return service.countActive()
                .map(count -> ResponseEntity.ok(count));
    }

    @GetMapping("/count/inactive")
    public Mono<ResponseEntity<Long>> countInactive() {
        return service.countInactive()
                .map(count -> ResponseEntity.ok(count));
    }

    // Actions sur les POI
    @PatchMapping("/{id}/activate")
    public Mono<ResponseEntity<PointOfInterestDTO>> activate(@PathVariable UUID id) {
        return service.activate(id)
                .map(dto -> ResponseEntity.ok(dto))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<ResponseEntity<PointOfInterestDTO>> deactivate(@PathVariable UUID id) {
        return service.deactivate(id)
                .map(dto -> ResponseEntity.ok(dto))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/popularity")
    public Mono<ResponseEntity<PointOfInterestDTO>> updatePopularityScore(
            @PathVariable UUID id,
            @RequestParam Float newScore) {
        return service.updatePopularityScore(id, newScore)
                .map(dto -> ResponseEntity.ok(dto))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // Endpoints de streaming pour de gros volumes
    @GetMapping(value = "/stream", produces = "application/stream+json")
    public Flux<PointOfInterestDTO> streamAll() {
        return service.findAll();
    }

    @GetMapping(value = "/organization/{organizationId}/stream", produces = "application/stream+json")
    public Flux<PointOfInterestDTO> streamByOrganization(@PathVariable UUID organizationId) {
        return service.findByOrganizationId(organizationId);
    }
}