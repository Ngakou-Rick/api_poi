package com.poi.yow_point.repositories;

import com.poi.yow_point.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findByPointOfInterestPoiId(UUID poiId, Pageable pageable);
    List<Review> findByPointOfInterestPoiId(UUID poiId); // Pour tous les avis d'un POI
    Page<Review> findByUserUserId(String userId, Pageable pageable);
}