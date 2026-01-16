package com.poi.yow_point.domain.ports.out;

import com.poi.yow_point.domain.model.Organization;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface OrganizationRepositoryPort {
    Mono<Organization> save(Organization organization);
    Mono<Organization> findById(UUID id);
    Flux<Organization> findAll();
    Mono<Void> deleteById(UUID id);
    Mono<Boolean> existsById(UUID id);
    Mono<Organization> findByOrgCode(String orgCode);
    Flux<Organization> findByOrgType(String orgType);
    Flux<Organization> findByIsActive(Boolean isActive);
    Flux<Organization> findByOrgName(String orgName);
}
