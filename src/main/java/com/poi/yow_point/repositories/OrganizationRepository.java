package com.poi.yow_point.repositories;

import com.poi.yow_point.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;



@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    // Basic CRUD methods are inherited
    Optional<Organization> findByOrgCode(String orgCode);
}


