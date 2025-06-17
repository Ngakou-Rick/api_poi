package com.yowyob.yowyob_point_of_interest_api.repository;

import com.yowyob.yowyob_point_of_interest_api.model.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PointOfInterestRepository extends JpaRepository<PointOfInterest, UUID> {
    List<PointOfInterest> findByPoiNameContainingIgnoreCase(String name);
    List<PointOfInterest> findByPoiType(String type);
    List<PointOfInterest> findByPoiCategory(String category);
    // TODO: Add method for spatial search ST_DWithin if using hibernate-spatial
    // e.g. List<PointOfInterest> findByLocationWithin(Geometry point, double distance);
}
