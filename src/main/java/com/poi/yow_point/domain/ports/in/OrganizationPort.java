package com.poi.yow_point.domain.ports.in;

import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.OrganizationDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrganizationPort {

    Mono<OrganizationDTO> saveOrganization(OrganizationDTO organizationDTO);

    Mono<OrganizationDTO> getOrganizationById(UUID id);

    Flux<OrganizationDTO> getAllOrganizations();

    Mono<OrganizationDTO> updateOrganization(UUID id, OrganizationDTO organizationDTO);

    Mono<Void> deleteOrganization(UUID id);

    Mono<OrganizationDTO> getOrganizationByOrgCode(String orgCode);

    Flux<OrganizationDTO> getOrganizationsByType(String orgType);

    Flux<OrganizationDTO> getOrganizationsByActiveStatus(Boolean isActive);

    Flux<OrganizationDTO> searchOrganizationsByName(String orgName);

}