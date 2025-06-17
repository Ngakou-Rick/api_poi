package com.poi.yow_point.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poi.yow_point.models.PoiPlatformStat;

import java.util.UUID;

@Repository
public interface PoiPlatformStatRepository extends JpaRepository<PoiPlatformStat, UUID> {
    // Basic CRUD methods are inherited
}
