package com.poi.yow_point.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poi.yow_point.models.PoiAccessLog;

import java.util.UUID;

@Repository
public interface PoiAccessLogRepository extends JpaRepository<PoiAccessLog, UUID> {
    // Basic CRUD methods are inherited
}
