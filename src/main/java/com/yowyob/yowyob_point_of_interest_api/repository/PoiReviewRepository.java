package com.yowyob.yowyob_point_of_interest_api.repository;

import com.yowyob.yowyob_point_of_interest_api.model.PoiReview;
// Removed PointOfInterest and AppUser imports as we use IDs now for query methods
import org.springframework.data.r2dbc.repository.R2dbcRepository; // Changed
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux; // Changed from List

import java.util.UUID;

@Repository
public interface PoiReviewRepository extends R2dbcRepository<PoiReview, UUID> { // Changed
    Flux<PoiReview> findByPoiId(UUID poiId); // Changed from findByPointOfInterest
    Flux<PoiReview> findByUserId(UUID userId); // Changed from findByUser
}
