package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
//import org.springframework.stereotype.Repository;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.OrganizationEntity;

import java.util.UUID;

public interface OrganizationRepository extends R2dbcRepository<OrganizationEntity, UUID>, OrganizationRepositoryCustom {

}