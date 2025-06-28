package com.poi.yow_point.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.poi.yow_point.dto.PoiReviewDTO;
import com.poi.yow_point.mappers.PoiReviewMapper;
import com.poi.yow_point.models.PoiReview;
import com.poi.yow_point.repositories.PoiReviewRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PoiReviewService {

    private final PoiReviewRepository poiReviewRepository;
    private final PoiReviewMapper poiReviewMapper;

    public Mono<PoiReviewDTO> createReview(PoiReviewDTO reviewDTO) {
        log.info("Creating review for POI: {}", reviewDTO.getPoiId());

        PoiReview review = poiReviewMapper.toEntity(reviewDTO);
        review.setReviewId(UUID.randomUUID());
        review.setCreatedAt(OffsetDateTime.now());

        return poiReviewRepository.save(review)
                .map(poiReviewMapper::toDTO)
                .doOnSuccess(savedReview -> log.info("Review created with ID: {}", savedReview.getReviewId()))
                .doOnError(error -> log.error("Error creating review: {}", error.getMessage()));
    }

    public Mono<PoiReviewDTO> getReviewById(UUID reviewId) {
        log.info("Fetching review with ID: {}", reviewId);

        return poiReviewRepository.findById(reviewId)
                .map(poiReviewMapper::toDTO)
                .doOnSuccess(review -> log.info("Review found: {}", reviewId))
                .switchIfEmpty(Mono.error(new RuntimeException("Review not found with ID: " + reviewId)));
    }

    public Flux<PoiReviewDTO> getAllReviews() {
        log.info("Fetching all reviews");

        return poiReviewRepository.findAll()
                .map(poiReviewMapper::toDTO)
                .doOnComplete(() -> log.info("All reviews fetched"));
    }

    public Flux<PoiReviewDTO> getReviewsByPoiId(UUID poiId) {
        log.info("Fetching reviews for POI: {}", poiId);

        return poiReviewRepository.findByPoiIdOrderByCreatedAtDesc(poiId)
                .map(poiReviewMapper::toDTO)
                .doOnComplete(() -> log.info("Reviews fetched for POI: {}", poiId));
    }

    public Flux<PoiReviewDTO> getReviewsByUserId(UUID userId) {
        log.info("Fetching reviews for user: {}", userId);

        return poiReviewRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .map(poiReviewMapper::toDTO)
                .doOnComplete(() -> log.info("Reviews fetched for user: {}", userId));
    }

    public Flux<PoiReviewDTO> getReviewsByOrganizationId(UUID organizationId) {
        log.info("Fetching reviews for organization: {}", organizationId);

        return poiReviewRepository.findByOrganizationId(organizationId)
                .map(poiReviewMapper::toDTO)
                .doOnComplete(() -> log.info("Reviews fetched for organization: {}", organizationId));
    }

    public Mono<PoiReviewDTO> updateReview(UUID reviewId, PoiReviewDTO reviewDTO) {
        log.info("Updating review with ID: {}", reviewId);

        return poiReviewRepository.findById(reviewId)
                .switchIfEmpty(Mono.error(new RuntimeException("Review not found with ID: " + reviewId)))
                .map(existingReview -> {
                    // Mettre à jour les champs modifiables
                    if (reviewDTO.getRating() != null) {
                        existingReview.setRating(reviewDTO.getRating());
                    }
                    if (reviewDTO.getReviewText() != null) {
                        existingReview.setReviewText(reviewDTO.getReviewText());
                    }
                    if (reviewDTO.getLikes() != null) {
                        existingReview.setLikes(reviewDTO.getLikes());
                    }
                    if (reviewDTO.getDislikes() != null) {
                        existingReview.setDislikes(reviewDTO.getDislikes());
                    }
                    return existingReview;
                })
                .flatMap(poiReviewRepository::save)
                .map(poiReviewMapper::toDTO)
                .doOnSuccess(updatedReview -> log.info("Review updated: {}", reviewId))
                .doOnError(error -> log.error("Error updating review {}: {}", reviewId, error.getMessage()));
    }

    public Mono<Void> deleteReview(UUID reviewId) {
        log.info("Deleting review with ID: {}", reviewId);

        return poiReviewRepository.existsById(reviewId)
                .flatMap(exists -> {
                    if (exists) {
                        return poiReviewRepository.deleteById(reviewId);
                    } else {
                        return Mono.error(new RuntimeException("Review not found with ID: " + reviewId));
                    }
                })
                .doOnSuccess(unused -> log.info("Review deleted: {}", reviewId))
                .doOnError(error -> log.error("Error deleting review {}: {}", reviewId, error.getMessage()));
    }

    public Mono<Double> getAverageRatingByPoiId(UUID poiId) {
        log.info("Calculating average rating for POI: {}", poiId);

        return poiReviewRepository.findAverageRatingByPoiId(poiId)
                .defaultIfEmpty(0.0)
                .doOnSuccess(avgRating -> log.info("Average rating for POI {}: {}", poiId, avgRating));
    }

    public Mono<Long> getReviewCountByPoiId(UUID poiId) {
        log.info("Counting reviews for POI: {}", poiId);

        return poiReviewRepository.countByPoiId(poiId)
                .doOnSuccess(count -> log.info("Review count for POI {}: {}", poiId, count));
    }

    public Mono<PoiReviewDTO> incrementLikes(UUID reviewId) {
        log.info("Incrementing likes for review: {}", reviewId);

        return poiReviewRepository.findById(reviewId)
                .switchIfEmpty(Mono.error(new RuntimeException("Review not found with ID: " + reviewId)))
                .map(review -> {
                    review.setLikes(review.getLikes() + 1);
                    return review;
                })
                .flatMap(poiReviewRepository::save)
                .map(poiReviewMapper::toDTO)
                .doOnSuccess(review -> log.info("Likes incremented for review: {}", reviewId));
    }

    public Mono<PoiReviewDTO> incrementDislikes(UUID reviewId) {
        log.info("Incrementing dislikes for review: {}", reviewId);

        return poiReviewRepository.findById(reviewId)
                .switchIfEmpty(Mono.error(new RuntimeException("Review not found with ID: " + reviewId)))
                .map(review -> {
                    review.setDislikes(review.getDislikes() + 1);
                    return review;
                })
                .flatMap(poiReviewRepository::save)
                .map(poiReviewMapper::toDTO)
                .doOnSuccess(review -> log.info("Dislikes incremented for review: {}", reviewId));
    }
}