package com.poi.yow_point.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.poi.yow_point.dto.ReviewDTO;
import com.poi.yow_point.models.Review;
import com.poi.yow_point.services.ReviewService;


import java.security.Principal;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewDTO reviewRequest, Principal principal) {
        // Principal principal (ou @AuthenticationPrincipal Jwt jwt) pour obtenir l'ID de l'utilisateur authentifié
        String userId = principal.getName(); // Assumant que getName() retourne l'ID utilisateur
        Review createdReview = reviewService.createReview(reviewRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @GetMapping("/poi/{poiId}")
    public ResponseEntity<Page<Review>> getReviewsForPoi(@PathVariable UUID poiId, Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsForPoi(poiId, pageable));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewById(@PathVariable UUID reviewId) {
        return reviewService.getReviewById(reviewId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable UUID reviewId,
                                               @Valid @RequestBody ReviewDTO reviewRequest,
                                               Principal principal) {
        String userId = principal.getName();
        try {
            Review updatedReview = reviewService.updateReview(reviewId, reviewRequest, userId);
            return ResponseEntity.ok(updatedReview);
        } catch (RuntimeException e) { // Attraper des exceptions plus spécifiques
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Ou NOT_FOUND
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId, Principal principal) {
        String userId = principal.getName();
        try {
            reviewService.deleteReview(reviewId, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Ou NOT_FOUND
        }
    }

    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Review> likeReview(@PathVariable UUID reviewId) {
        try {
            return ResponseEntity.ok(reviewService.likeReview(reviewId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{reviewId}/dislike")
    public ResponseEntity<Review> dislikeReview(@PathVariable UUID reviewId) {
         try {
            return ResponseEntity.ok(reviewService.dislikeReview(reviewId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}