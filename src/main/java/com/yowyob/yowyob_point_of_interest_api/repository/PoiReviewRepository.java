package com.yowyob.yowyob_point_of_interest_api.repository;

import com.yowyob.yowyob_point_of_interest_api.model.PoiReview;
import com.yowyob.yowyob_point_of_interest_api.model.PointOfInterest;
import com.yowyob.yowyob_point_of_interest_api.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PoiReviewRepository extends JpaRepository<PoiReview, UUID> {
    List<PoiReview> findByPointOfInterest(PointOfInterest pointOfInterest);
    List<PoiReview> findByUser(AppUser user);
}
