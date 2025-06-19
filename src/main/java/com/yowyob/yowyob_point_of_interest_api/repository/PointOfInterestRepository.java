package com.yowyob.yowyob_point_of_interest_api.repository;

import com.yowyob.yowyob_point_of_interest_api.model.PointOfInterest;
// Remove import org.locationtech.jts.geom.Point;
import org.springframework.data.r2dbc.repository.Query; // Added
import org.springframework.data.r2dbc.repository.R2dbcRepository; // Changed
import org.springframework.data.repository.query.Param; // Added
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux; // Changed from List

import java.util.UUID;

@Repository
public interface PointOfInterestRepository extends R2dbcRepository<PointOfInterest, UUID> { // Changed
    Flux<PointOfInterest> findByPoiNameContainingIgnoreCase(String name); // Changed from List
    Flux<PointOfInterest> findByPoiType(String type); // Changed from List
    Flux<PointOfInterest> findByPoiCategory(String category); // Changed from List

    // ST_DWithin expects geometry, not geography, for WKT string input if not casting explicitly.
    // Using ST_GeomFromText for the input point.
    @Query(value = "SELECT * FROM point_of_interest p WHERE ST_DWithin(p.location_geog, ST_SetSRID(ST_GeomFromText(:wktPoint), 4326), :distance, false)")
    Flux<PointOfInterest> findNearby(@Param("wktPoint") String wktPoint, @Param("distance") double distance); // Parameter changed

    Flux<PointOfInterest> findByTownId(UUID townId); // Changed from List

    // For flattened address, query would be on direct columns
    @Query("SELECT * FROM point_of_interest WHERE lower(poi_address_state_province) = lower(:stateProvince)")
    Flux<PointOfInterest> findByPoiAddressStateProvinceIgnoreCase(@Param("stateProvince") String stateProvince); // Changed from List

    Flux<PointOfInterest> findByCreatedByUserId(UUID userId); // Changed from List
    Flux<PointOfInterest> findAllByOrderByPopularityScoreDesc(); // Changed from List
    Flux<PointOfInterest> findByIsActiveTrueOrderByPopularityScoreDesc(); // Changed from List
}
