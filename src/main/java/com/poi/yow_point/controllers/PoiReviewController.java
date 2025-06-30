package com.poi.yow_point.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

import com.poi.yow_point.dto.PoiReviewDTO;
import com.poi.yow_point.services.PoiReviewService;

import jakarta.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "POI Reviews", description = "API pour la gestion des avis et commentaires sur les points d'intérêt")
public class PoiReviewController {

    private final PoiReviewService poiReviewService;

    @PostMapping
    @Operation(summary = "Créer un nouvel avis", description = "Crée un nouvel avis pour un point d'intérêt (POI)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avis créé avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
            @ApiResponse(responseCode = "422", description = "Erreur de validation", content = @Content)
    })
    public Mono<ResponseEntity<PoiReviewDTO>> createReview(
            @Valid @RequestBody(description = "Données de l'avis à créer", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class))) @org.springframework.web.bind.annotation.RequestBody PoiReviewDTO reviewDTO) {
        log.info("POST /api/reviews - Creating review for POI: {}", reviewDTO.getPoiId());

        return poiReviewService.createReview(reviewDTO)
                .map(createdReview -> ResponseEntity.status(HttpStatus.CREATED).body(createdReview))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "Récupérer un avis par ID", description = "Récupère un avis spécifique par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avis trouvé", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class))),
            @ApiResponse(responseCode = "404", description = "Avis non trouvé", content = @Content)
    })
    public Mono<ResponseEntity<PoiReviewDTO>> getReviewById(
            @Parameter(description = "Identifiant unique de l'avis", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID reviewId) {
        log.info("GET /api/reviews/{} - Fetching review", reviewId);

        return poiReviewService.getReviewById(reviewId)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les avis", description = "Récupère la liste complète des avis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des avis récupérée avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class)))
    })
    public Flux<PoiReviewDTO> getAllReviews() {
        log.info("GET /api/reviews - Fetching all reviews");

        return poiReviewService.getAllReviews();
    }

    @GetMapping("/poi/{poiId}")
    @Operation(summary = "Récupérer les avis d'un POI", description = "Récupère tous les avis associés à un point d'intérêt spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des avis du POI récupérée avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class)))
    })
    public Flux<PoiReviewDTO> getReviewsByPoiId(
            @Parameter(description = "Identifiant unique du POI", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId) {
        log.info("GET /api/reviews/poi/{} - Fetching reviews for POI", poiId);

        return poiReviewService.getReviewsByPoiId(poiId);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Récupérer les avis d'un utilisateur", description = "Récupère tous les avis créés par un utilisateur spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des avis de l'utilisateur récupérée avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class)))
    })
    public Flux<PoiReviewDTO> getReviewsByUserId(
            @Parameter(description = "Identifiant unique de l'utilisateur", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID userId) {
        log.info("GET /api/reviews/user/{} - Fetching reviews for user", userId);

        return poiReviewService.getReviewsByUserId(userId);
    }

    @GetMapping("/organization/{organizationId}")
    @Operation(summary = "Récupérer les avis d'une organisation", description = "Récupère tous les avis associés aux POI d'une organisation spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des avis de l'organisation récupérée avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class)))
    })
    public Flux<PoiReviewDTO> getReviewsByOrganizationId(
            @Parameter(description = "Identifiant unique de l'organisation", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID organizationId) {
        log.info("GET /api/reviews/organization/{} - Fetching reviews for organization", organizationId);

        return poiReviewService.getReviewsByOrganizationId(organizationId);
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Mettre à jour un avis", description = "Met à jour complètement un avis existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avis mis à jour avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class))),
            @ApiResponse(responseCode = "404", description = "Avis non trouvé", content = @Content),
            @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
    })
    public Mono<ResponseEntity<PoiReviewDTO>> updateReview(
            @Parameter(description = "Identifiant unique de l'avis à mettre à jour", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID reviewId,
            @Valid @RequestBody(description = "Nouvelles données de l'avis", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class))) @org.springframework.web.bind.annotation.RequestBody PoiReviewDTO reviewDTO) {
        log.info("PUT /api/reviews/{} - Updating review", reviewId);

        return poiReviewService.updateReview(reviewId, reviewDTO)
                .map(updatedReview -> ResponseEntity.ok(updatedReview))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Supprimer un avis", description = "Supprime définitivement un avis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Avis supprimé avec succès", content = @Content),
            @ApiResponse(responseCode = "404", description = "Avis non trouvé", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteReview(
            @Parameter(description = "Identifiant unique de l'avis à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID reviewId) {
        log.info("DELETE /api/reviews/{} - Deleting review", reviewId);

        return poiReviewService.deleteReview(reviewId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/poi/{poiId}/average-rating")
    @Operation(summary = "Calculer la note moyenne d'un POI", description = "Calcule et retourne la note moyenne de tous les avis d'un POI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note moyenne calculée avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Double.class, example = "4.2")))
    })
    public Mono<ResponseEntity<Double>> getAverageRatingByPoiId(
            @Parameter(description = "Identifiant unique du POI", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId) {
        log.info("GET /api/reviews/poi/{}/average-rating - Getting average rating", poiId);

        return poiReviewService.getAverageRatingByPoiId(poiId)
                .map(avgRating -> ResponseEntity.ok(avgRating));
    }

    @GetMapping("/poi/{poiId}/count")
    @Operation(summary = "Compter les avis d'un POI", description = "Retourne le nombre total d'avis pour un POI spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre d'avis récupéré avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Long.class, example = "42")))
    })
    public Mono<ResponseEntity<Long>> getReviewCountByPoiId(
            @Parameter(description = "Identifiant unique du POI", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId) {
        log.info("GET /api/reviews/poi/{}/count - Getting review count", poiId);

        return poiReviewService.getReviewCountByPoiId(poiId)
                .map(count -> ResponseEntity.ok(count));
    }

    @PatchMapping("/{reviewId}/like")
    @Operation(summary = "Aimer un avis", description = "Incrémente le compteur de likes d'un avis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like ajouté avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class))),
            @ApiResponse(responseCode = "404", description = "Avis non trouvé", content = @Content)
    })
    public Mono<ResponseEntity<PoiReviewDTO>> incrementLikes(
            @Parameter(description = "Identifiant unique de l'avis", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID reviewId) {
        log.info("PATCH /api/reviews/{}/like - Incrementing likes", reviewId);

        return poiReviewService.incrementLikes(reviewId)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{reviewId}/dislike")
    @Operation(summary = "Ne pas aimer un avis", description = "Incrémente le compteur de dislikes d'un avis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dislike ajouté avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PoiReviewDTO.class))),
            @ApiResponse(responseCode = "404", description = "Avis non trouvé", content = @Content)
    })
    public Mono<ResponseEntity<PoiReviewDTO>> incrementDislikes(
            @Parameter(description = "Identifiant unique de l'avis", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID reviewId) {
        log.info("PATCH /api/reviews/{}/dislike - Incrementing dislikes", reviewId);

        return poiReviewService.incrementDislikes(reviewId)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/poi/{poiId}/stats")
    @Operation(summary = "Statistiques d'un POI", description = "Récupère les statistiques complètes des avis d'un POI (note moyenne et nombre d'avis)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées avec succès", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(example = """
                    {
                        "averageRating": 4.2,
                        "reviewCount": 42
                    }
                    """)))
    })
    public Mono<ResponseEntity<Object>> getPoiReviewStats(
            @Parameter(description = "Identifiant unique du POI", required = true, example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID poiId) {
        log.info("GET /api/reviews/poi/{}/stats - Getting review statistics", poiId);

        return Mono.zip(
                poiReviewService.getAverageRatingByPoiId(poiId),
                poiReviewService.getReviewCountByPoiId(poiId))
                .map(tuple -> {
                    var stats = new Object() {
                        public final Double averageRating = tuple.getT1();
                        public final Long reviewCount = tuple.getT2();
                    };
                    return ResponseEntity.ok((Object) stats);
                });
    }
}