package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repositoryUser;

import java.util.UUID;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.AppUserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppUserRepositoryCustom {
    Mono<AppUserEntity> findByUsername(String username);

    Mono<AppUserEntity> findByEmail(String email);

    Mono<Boolean> existsByOrgId(UUID orgId);

    Mono<Long> countActiveUsersByOrgId(UUID orgId);

    Flux<AppUserEntity> findByOrgIdAndIsActive(UUID orgId, Boolean isActive);

    Flux<AppUserEntity> findByRole(String role);
}
