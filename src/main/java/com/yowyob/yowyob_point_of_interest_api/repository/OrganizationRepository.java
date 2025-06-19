package com.yowyob.yowyob_point_of_interest_api.repository;

import com.yowyob.yowyob_point_of_interest_api.model.Organization;
import org.springframework.data.r2dbc.repository.R2dbcRepository; // Changed
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux; // Added
import reactor.core.publisher.Mono; // Added

import java.util.UUID;

@Repository
public interface OrganizationRepository extends R2dbcRepository<Organization, UUID> { // Changed
    // Add custom reactive queries if needed, e.g.:
    // Flux<Organization> findByOrgNameContainingIgnoreCase(String name);
    // Mono<Organization> findByOrgCode(String orgCode);
}
