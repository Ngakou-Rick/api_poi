package com.poi.yow_point.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poi.yow_point.models.PointOfInterest;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.UUID;

@Repository
public interface PointOfInterestRepository extends JpaRepository<PointOfInterest, UUID> {
    List<PointOfInterest> findByPoiNameContainingIgnoreCase(String name);
    List<PointOfInterest> findByPoiTypeIgnoreCase(String type);
    List<PointOfInterest> findByPoiCategoryIgnoreCase(String category);
    
    @Query(value = "SELECT p.* FROM point_of_interest p WHERE ST_DWithin(p.location_geog, :point, :distance, false)", nativeQuery = true)
    List<PointOfInterest> findNearby(@Param("point") Point point, @Param("distance") double distance);

    List<PointOfInterest> findByTownId(UUID townId);
    List<PointOfInterest> findByPoiAddressStateProvinceIgnoreCase(String stateProvince);
    List<PointOfInterest> findByCreatedByUserId(UUID userId);
    List<PointOfInterest> findAllByOrderByPopularityScoreDesc();
    List<PointOfInterest> findByIsActiveTrueOrderByPopularityScoreDesc(); // Example combining with active status
}