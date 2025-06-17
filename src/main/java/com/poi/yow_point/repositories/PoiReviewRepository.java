package com.poi.yow_point.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poi.yow_point.models.AppUser;
import com.poi.yow_point.models.PoiReview;
import com.poi.yow_point.models.PointOfInterest;

import java.util.List;
import java.util.UUID;

@Repository
public interface PoiReviewRepository extends JpaRepository<PoiReview, UUID> {
    List<PoiReview> findByPointOfInterest(PointOfInterest pointOfInterest);
    List<PoiReview> findByUser(AppUser user);
}
