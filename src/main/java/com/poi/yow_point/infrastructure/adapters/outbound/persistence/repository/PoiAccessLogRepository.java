package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
//import org.springframework.stereotype.Repository;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PoiAccessLogEntity;
import java.util.UUID;

public interface PoiAccessLogRepository extends R2dbcRepository<PoiAccessLogEntity, UUID>, PoiAccessLogRepositoryCustom {

}