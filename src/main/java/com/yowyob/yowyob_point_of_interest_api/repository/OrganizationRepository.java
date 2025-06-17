package com.yowyob.yowyob_point_of_interest_api.repository;

import com.yowyob.yowyob_point_of_interest_api.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    // Basic CRUD methods are inherited
}
