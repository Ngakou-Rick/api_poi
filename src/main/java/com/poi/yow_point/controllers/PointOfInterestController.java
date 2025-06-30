package com.poi.yow_point.controllers;

import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.services.PointOfInterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pois")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Points d'Intérêt", description = "API de gestion des points d'intérêt (POI)")
public class PointOfInterestController {

    private final PointOfInterestService poiService;

    @PostMapping
    @Operation(summary = "Créer un nouveau POI", description = "Crée un nouveau point d'intérêt avec les informations fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "POI créé avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Mono<ResponseEntity<PointOfInterestDTO>> createPoi(
            @Parameter(description = "Données du POI à créer", required = true) @Valid @RequestBody PointOfInterestDTO dto) {
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

    @PutMapping("/{poiId}")
    @Operation(summary = "Mettre à jour un POI", description = "Met à jour un point d'intérêt existant avec les nouvelles informations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POI mis à jour avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
            @ApiResponse(responseCode = "404", description = "POI non trouvé", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Mono<ResponseEntity<PointOfInterestDTO>> updatePoi(
            @Parameter(description = "ID du POI à mettre à jour", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId,
            @Parameter(description = "Nouvelles données du POI", required = true) @Valid @RequestBody PointOfInterestDTO dto) {
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

    @GetMapping("/{poiId}")
    @Operation(summary = "Récupérer un POI par ID", description = "Récupère les détails d'un point d'intérêt spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POI trouvé", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "POI non trouvé", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Mono<ResponseEntity<PointOfInterestDTO>> getPoiById(
            @Parameter(description = "ID du POI", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId) {
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

    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Récupérer les POIs actifs d'une organisation", description = "Récupère tous les points d'intérêt actifs appartenant à une organisation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des POIs actifs", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> getPoisByOrganization(
            @Parameter(description = "ID de l'organisation", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID organizationId) {
        log.debug("REST request to get POIs for organization: {}", organizationId);

        return poiService.findActiveByOrganizationId(organizationId)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs for organization: {}", organizationId, ex);
                            return Flux.empty();
                        });
    }

    @GetMapping("/organization/{organizationId}/all")
    @Operation(summary = "Récupérer tous les POIs d'une organisation", description = "Récupère tous les points d'intérêt (actifs et inactifs) appartenant à une organisation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste de tous les POIs", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> getAllPoisByOrganization(
            @Parameter(description = "ID de l'organisation", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID organizationId) {
        log.debug("REST request to get all POIs for organization: {}", organizationId);

        return poiService.findByOrganizationId(organizationId)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving all POIs for organization: {}", organizationId, ex);
                            return Flux.empty();
                        });
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des POIs avec filtres", description = "Recherche des points d'intérêt en utilisant plusieurs critères de filtrage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultats de la recherche", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> searchPois(
            @Parameter(description = "ID de l'organisation", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam(required = false) UUID organizationId,
            @Parameter(description = "Type de POI", example = "restaurant") @RequestParam(required = false) String type,
            @Parameter(description = "Catégorie de POI", example = "gastronomie") @RequestParam(required = false) String category,
            @Parameter(description = "Ville", example = "Yaoundé") @RequestParam(required = false) String city,
            @Parameter(description = "Terme de recherche dans le nom ou la description", example = "hotel") @RequestParam(required = false) String searchTerm) {
        log.debug("REST request to search POIs with filters");

        return poiService.searchWithFilters(organizationId, type, category, city, searchTerm)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error in POI search", ex);
                            return Flux.empty();
                        });
    }

    @GetMapping("/location")
    @Operation(summary = "Rechercher des POIs par localisation", description = "Recherche des points d'intérêt dans un rayon donné autour d'une position géographique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POIs trouvés dans la zone", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Paramètres de localisation invalides", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> getPoisByLocation(
            @Parameter(description = "Latitude", required = true, example = "3.8480") @RequestParam BigDecimal latitude,
            @Parameter(description = "Longitude", required = true, example = "11.5021") @RequestParam BigDecimal longitude,
            @Parameter(description = "Rayon de recherche en kilomètres", example = "5.0") @RequestParam(defaultValue = "10.0") Double radiusKm) {
        log.debug("REST request to get POIs by location: {}, {} within {} km",
                latitude, longitude, radiusKm);

        return poiService.findByLocationWithinRadius(latitude, longitude, radiusKm)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error in location-based POI search", ex);
                            return Flux.empty();
                        });
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Récupérer les POIs par type", description = "Récupère tous les points d'intérêt d'un type spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des POIs du type spécifié", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> getPoisByType(
            @Parameter(description = "Type de POI", required = true, example = "restaurant") @PathVariable String type) {
        log.debug("REST request to get POIs by type: {}", type);

        return poiService.findByType(type)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs by type: {}", type, ex);
                            return Flux.empty();
                        });
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Récupérer les POIs par catégorie", description = "Récupère tous les points d'intérêt d'une catégorie spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des POIs de la catégorie spécifiée", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> getPoisByCategory(
            @Parameter(description = "Catégorie de POI", required = true, example = "gastronomie") @PathVariable String category) {
        log.debug("REST request to get POIs by category: {}", category);

        return poiService.findByCategory(category)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs by category: {}", category, ex);
                            return Flux.empty();
                        });
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Rechercher des POIs par nom", description = "Recherche des points d'intérêt par leur nom (recherche partielle)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POIs correspondant au nom recherché", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> searchPoisByName(
            @Parameter(description = "Nom ou partie du nom à rechercher", required = true, example = "hotel") @PathVariable String name) {
        log.debug("REST request to search POIs by name: {}", name);

        return poiService.searchByName(name)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error searching POIs by name: {}", name, ex);
                            return Flux.empty();
                        });
    }

    @GetMapping("/city/{city}")
    @Operation(summary = "Récupérer les POIs par ville", description = "Récupère tous les points d'intérêt situés dans une ville spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des POIs de la ville", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> getPoisByCity(
            @Parameter(description = "Nom de la ville", required = true, example = "Yaoundé") @PathVariable String city) {
        log.debug("REST request to get POIs by city: {}", city);

        return poiService.findByCity(city)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs by city: {}", city, ex);
                            return Flux.empty();
                        });
    }

    @GetMapping("/popular")
    @Operation(summary = "Récupérer les POIs les plus populaires", description = "Récupère les points d'intérêt les mieux notés/les plus populaires")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des POIs populaires", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> getTopPopularPois(
            @Parameter(description = "Nombre maximum de POIs à retourner", example = "10") @RequestParam(defaultValue = "10") Integer limit) {
        log.debug("REST request to get top {} popular POIs", limit);

        return poiService.findTopPopular(limit)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving popular POIs", ex);
                            return Flux.empty();
                        });
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Récupérer les POIs créés par un utilisateur", description = "Récupère tous les points d'intérêt créés par un utilisateur spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des POIs créés par l'utilisateur", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Flux<PointOfInterestDTO> getPoisByUser(
            @Parameter(description = "ID de l'utilisateur", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID userId) {
        log.debug("REST request to get POIs created by user: {}", userId);

        return poiService.findByCreatedByUserId(userId)
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error retrieving POIs for user: {}", userId, ex);
                            return Flux.empty();
                        });
    }

    @PatchMapping("/{poiId}/deactivate")
    @Operation(summary = "Désactiver un POI", description = "Désactive un point d'intérêt (suppression logique)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POI désactivé avec succès"),
            @ApiResponse(responseCode = "404", description = "POI non trouvé", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deactivatePoi(
            @Parameter(description = "ID du POI à désactiver", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId) {
        log.info("REST request to deactivate POI: {}", poiId);

        return poiService.deactivatePoi(poiId)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error deactivating POI: {}", poiId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    @PatchMapping("/{poiId}/activate")
    @Operation(summary = "Réactiver un POI", description = "Réactive un point d'intérêt précédemment désactivé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POI réactivé avec succès"),
            @ApiResponse(responseCode = "404", description = "POI non trouvé", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Mono<ResponseEntity<Void>> activatePoi(
            @Parameter(description = "ID du POI à réactiver", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId) {
        log.info("REST request to activate POI: {}", poiId);

        return poiService.activatePoi(poiId)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error activating POI: {}", poiId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    @DeleteMapping("/{poiId}")
    @Operation(summary = "Supprimer définitivement un POI", description = "Supprime définitivement un point d'intérêt de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "POI supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "POI non trouvé", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deletePoi(
            @Parameter(description = "ID du POI à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId) {
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

    @PatchMapping("/{poiId}/popularity")
    @Operation(summary = "Mettre à jour le score de popularité", description = "Met à jour le score de popularité d'un point d'intérêt (0-100)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Score de popularité mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Score invalide (doit être entre 0 et 100)", content = @Content),
            @ApiResponse(responseCode = "404", description = "POI non trouvé", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Mono<ResponseEntity<Void>> updatePopularityScore(
            @Parameter(description = "ID du POI", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId,
            @Parameter(description = "Nouveau score de popularité (0-100)", required = true, example = "85.5") @RequestParam Float score) {
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

    @GetMapping("/organization/{organizationId}/count")
    @Operation(summary = "Compter les POIs actifs d'une organisation", description = "Retourne le nombre de points d'intérêt actifs pour une organisation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de POIs actifs", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Mono<ResponseEntity<Long>> countActivePoisByOrganization(
            @Parameter(description = "ID de l'organisation", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID organizationId) {
        log.debug("REST request to count active POIs for organization: {}", organizationId);

        return poiService.countActiveByOrganizationId(organizationId)
                .map(count -> ResponseEntity.ok(count))
                .onErrorResume(Exception.class,
                        ex -> {
                            log.error("Error counting POIs for organization: {}", organizationId, ex);
                            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                        });
    }

    @GetMapping("/check-name")
    @Operation(summary = "Vérifier l'existence d'un nom de POI", description = "Vérifie si un nom de POI existe déjà dans une organisation (utile pour la validation)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultat de la vérification", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @Content)
    })
    public Mono<ResponseEntity<Boolean>> checkPoiNameExists(
            @Parameter(description = "Nom du POI à vérifier", required = true, example = "Hotel Hilton") @RequestParam String name,
            @Parameter(description = "ID de l'organisation", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam UUID organizationId,
            @Parameter(description = "ID du POI à exclure de la vérification (pour les mises à jour)", example = "123e4567-e89b-12d3-a456-426614174000") @RequestParam(required = false) UUID excludeId) {
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