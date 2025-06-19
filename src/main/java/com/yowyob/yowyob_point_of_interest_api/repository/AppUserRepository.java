package com.yowyob.yowyob_point_of_interest_api.repository;

import com.yowyob.yowyob_point_of_interest_api.model.AppUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository; // Changed
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono; // Changed from Optional

import java.util.UUID;

@Repository
public interface AppUserRepository extends R2dbcRepository<AppUser, UUID> { // Changed
    Mono<AppUser> findByUsername(String username); // Changed from Optional
    Mono<AppUser> findByEmail(String email); // Changed from Optional
}
