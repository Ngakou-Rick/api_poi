package com.poi.yow_point.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
public class PoiReviewController {

    private final PoiReviewService poiReviewService;

    @PostMapping
    public Mono<ResponseEntity<PoiReviewDTO>> createReview(@Valid @RequestBody PoiReviewDTO reviewDTO) {
        log.info("POST /api/reviews - Creating review for POI: {}", reviewDTO.getPoiId());

        return poiReviewService.createReview(reviewDTO)
                .map(createdReview -> ResponseEntity.status(HttpStatus.CREATED).body(createdReview))
                .onErrorReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/{reviewId}")
    public Mono<ResponseEntity<PoiReviewDTO>> getReviewById(@PathVariable UUID reviewId) {
        log.info("GET /api/reviews/{} - Fetching review", reviewId);

        return poiReviewService.getReviewById(reviewId)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<PoiReviewDTO> getAllReviews() {
        log.info("GET /api/reviews - Fetching all reviews");

        return poiReviewService.getAllReviews();
    }

    @GetMapping("/poi/{poiId}")
    public Flux<PoiReviewDTO> getReviewsByPoiId(@PathVariable UUID poiId) {
        log.info("GET /api/reviews/poi/{} - Fetching reviews for POI", poiId);

        return poiReviewService.getReviewsByPoiId(poiId);
    }

    @GetMapping("/user/{userId}")
    public Flux<PoiReviewDTO> getReviewsByUserId(@PathVariable UUID userId) {
        log.info("GET /api/reviews/user/{} - Fetching reviews for user", userId);

        return poiReviewService.getReviewsByUserId(userId);
    }

    @GetMapping("/organization/{organizationId}")
    public Flux<PoiReviewDTO> getReviewsByOrganizationId(@PathVariable UUID organizationId) {
        log.info("GET /api/reviews/organization/{} - Fetching reviews for organization", organizationId);

        return poiReviewService.getReviewsByOrganizationId(organizationId);
    }

    @PutMapping("/{reviewId}")
    public Mono<ResponseEntity<PoiReviewDTO>> updateReview(
            @PathVariable UUID reviewId,
            @Valid @RequestBody PoiReviewDTO reviewDTO) {
        log.info("PUT /api/reviews/{} - Updating review", reviewId);

        return poiReviewService.updateReview(reviewId, reviewDTO)
                .map(updatedReview -> ResponseEntity.ok(updatedReview))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{reviewId}")
    public Mono<ResponseEntity<Void>> deleteReview(@PathVariable UUID reviewId) {
        log.info("DELETE /api/reviews/{} - Deleting review", reviewId);

        return poiReviewService.deleteReview(reviewId)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/poi/{poiId}/average-rating")
    public Mono<ResponseEntity<Double>> getAverageRatingByPoiId(@PathVariable UUID poiId) {
        log.info("GET /api/reviews/poi/{}/average-rating - Getting average rating", poiId);

        return poiReviewService.getAverageRatingByPoiId(poiId)
                .map(avgRating -> ResponseEntity.ok(avgRating));
    }

    @GetMapping("/poi/{poiId}/count")
    public Mono<ResponseEntity<Long>> getReviewCountByPoiId(@PathVariable UUID poiId) {
        log.info("GET /api/reviews/poi/{}/count - Getting review count", poiId);

        return poiReviewService.getReviewCountByPoiId(poiId)
                .map(count -> ResponseEntity.ok(count));
    }

    @PatchMapping("/{reviewId}/like")
    public Mono<ResponseEntity<PoiReviewDTO>> incrementLikes(@PathVariable UUID reviewId) {
        log.info("PATCH /api/reviews/{}/like - Incrementing likes", reviewId);

        return poiReviewService.incrementLikes(reviewId)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{reviewId}/dislike")
    public Mono<ResponseEntity<PoiReviewDTO>> incrementDislikes(@PathVariable UUID reviewId) {
        log.info("PATCH /api/reviews/{}/dislike - Incrementing dislikes", reviewId);

        return poiReviewService.incrementDislikes(reviewId)
                .map(review -> ResponseEntity.ok(review))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // Endpoint pour obtenir des statistiques globales
    @GetMapping("/poi/{poiId}/stats")
    public Mono<ResponseEntity<Object>> getPoiReviewStats(@PathVariable UUID poiId) {
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