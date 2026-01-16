package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repositoryUser;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
//import org.springframework.stereotype.Repository;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.AppUserEntity;

import java.util.UUID;

public interface AppUserRepository extends R2dbcRepository<AppUserEntity, UUID>, AppUserRepositoryCustom {

}