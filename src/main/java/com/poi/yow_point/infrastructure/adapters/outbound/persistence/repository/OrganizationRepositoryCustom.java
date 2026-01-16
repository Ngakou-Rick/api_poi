package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.OrganizationEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrganizationRepositoryCustom {

    Flux<OrganizationEntity> findAllActive();

    // Méthode personnalisée pour rechercher par orgCode
    Mono<OrganizationEntity> findByOrgCode(String orgCode);

    // Méthode pour rechercher par type d'organisation
    Flux<OrganizationEntity> findByOrgType(String orgType);

    // Méthode pour rechercher par statut actif
    Flux<OrganizationEntity> findByIsActive(Boolean isActive);

    // Méthode pour rechercher par nom contenant
    Flux<OrganizationEntity> findByOrgName(String orgName);

    // Méthode pour rechercher par type et statut
    Flux<OrganizationEntity> findByOrgTypeAndIsActive(String orgType, Boolean isActive);
}
