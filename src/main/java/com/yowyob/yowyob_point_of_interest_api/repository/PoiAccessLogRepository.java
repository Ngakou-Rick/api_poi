package com.yowyob.yowyob_point_of_interest_api.repository;

import com.yowyob.yowyob_point_of_interest_api.model.PoiAccessLog;
import org.springframework.data.r2dbc.repository.R2dbcRepository; // Changed
import org.springframework.stereotype.Repository;
// import reactor.core.publisher.Flux; // Add if custom queries are needed

import java.util.UUID;

@Repository
public interface PoiAccessLogRepository extends R2dbcRepository<PoiAccessLog, UUID> { // Changed
    // Basic reactive CRUD methods are inherited
}
