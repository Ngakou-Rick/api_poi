package com.poi.yow_point.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poi.yow_point.dto.ReviewDTO;
import com.poi.yow_point.models.Poi;
import com.poi.yow_point.models.Review;
import com.poi.yow_point.models.User;
import com.poi.yow_point.repositories.PoiRepository;
import com.poi.yow_point.repositories.ReviewRepository;
import com.poi.yow_point.repositories.UserRepository;

import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PoiRepository poiRepository; // Assurez-vous que ce repo existe
    private final UserRepository userRepository;           // Pour récupérer l'entité User

    @Transactional
    public Review createReview(ReviewDTO reviewRequest, String authenticatedUserId) {
        Poi poi = poiRepository.findById(reviewRequest.getPoiId())
                .orElseThrow(() -> new RuntimeException("POI not found with id: " + reviewRequest.getPoiId()));
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + authenticatedUserId));

        Review review = Review.builder()
                .pointOfInterest(poi)
                .user(user)
                .rating(reviewRequest.getRating())
                .reviewText(reviewRequest.getReviewText())
                .build();
        return reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Page<Review> getReviewsForPoi(UUID poiId, Pageable pageable) {
        return reviewRepository.findByPointOfInterestPoiId(poiId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Review> getReviewById(UUID reviewId) {
        return reviewRepository.findById(reviewId);
    }

    @Transactional
    public Review updateReview(UUID reviewId, ReviewDTO reviewRequest, String authenticatedUserId) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Vérifier que l'utilisateur authentifié est bien le propriétaire de l'avis
        if (!existingReview.getUser().getUserId().equals(authenticatedUserId)) {
            throw new RuntimeException("User not authorized to update this review"); // Ou AccessDeniedException
        }
        // Vérifier que le POI de la requête correspond (ou ne pas permettre de changer de POI)
        if (!existingReview.getPointOfInterest().getPoiId().equals(reviewRequest.getPoiId())) {
             throw new RuntimeException("Cannot change the POI of a review");
        }


        existingReview.setRating(reviewRequest.getRating());
        existingReview.setReviewText(reviewRequest.getReviewText());
        // 'createdAt', 'likes', 'dislikes' ne sont généralement pas modifiables directement par l'utilisateur ici
        return reviewRepository.save(existingReview);
    }

    @Transactional
    public void deleteReview(UUID reviewId, String authenticatedUserId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getUserId().equals(authenticatedUserId)) { // Ou si l'utilisateur a un rôle admin
            throw new RuntimeException("User not authorized to delete this review");
        }
        reviewRepository.deleteById(reviewId);
    }

    // Méthodes pour 'like'/'dislike'
    @Transactional
    public Review likeReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setLikes(review.getLikes() + 1);
        return reviewRepository.save(review);
    }

    @Transactional
    public Review dislikeReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setDislikes(review.getDislikes() + 1);
        return reviewRepository.save(review);
    }
}