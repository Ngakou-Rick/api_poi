package com.yowyob.yowyob_point_of_interest_api.service;

import com.yowyob.yowyob_point_of_interest_api.dto.OrganizationDTO;
import com.yowyob.yowyob_point_of_interest_api.mapper.OrganizationMapper;
import com.yowyob.yowyob_point_of_interest_api.model.Organization;
import com.yowyob.yowyob_point_of_interest_api.repository.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Keep for conceptual boundary
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime; // Added
import java.util.UUID;

@Service
public class OrganizationService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationService.class);
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    @Transactional
    public Mono<OrganizationDTO> saveOrganization(OrganizationDTO organizationDTO) {
        log.info("Saving organization: {}", organizationDTO.getOrgName());
        Organization organization = organizationMapper.toEntity(organizationDTO);

        if (organization.getOrganizationId() == null) { // New organization
            organization.setOrganizationId(UUID.randomUUID()); // Assign ID if not DB generated
            organization.setCreatedAt(OffsetDateTime.now());
            if (organization.getIsActive() == null) {
                organization.setIsActive(true);
            }
        }
        // For updates, createdAt should not change. isActive is mapped from DTO.

        return organizationRepository.save(organization)
            .map(organizationMapper::toDTO)
            .doOnSuccess(dto -> log.info("Saved organization with ID: {}", dto.getOrganizationId()))
            .doOnError(e -> log.error("Error saving organization: {}", organizationDTO.getOrgName(), e));
    }

    public Mono<OrganizationDTO> getOrganizationById(UUID id) {
        log.info("Fetching organization by ID: {}", id);
        return organizationRepository.findById(id)
                                     .map(organizationMapper::toDTO);
    }

    public Flux<OrganizationDTO> getAllOrganizations() {
        log.info("Fetching all organizations");
        return organizationRepository.findAll()
                                     .map(organizationMapper::toDTO);
    }

    @Transactional
    public Mono<OrganizationDTO> updateOrganization(UUID id, OrganizationDTO organizationDTO) {
        log.info("Updating organization with ID: {}", id);
        return organizationRepository.findById(id)
            .flatMap(existingOrg -> {
                existingOrg.setOrgName(organizationDTO.getOrgName());
                existingOrg.setOrgCode(organizationDTO.getOrgCode());
                existingOrg.setOrgType(organizationDTO.getOrgType());
                if (organizationDTO.getIsActive() != null) { // Allow explicit update of isActive
                    existingOrg.setIsActive(organizationDTO.getIsActive());
                }
                // createdAt is not updated
                return organizationRepository.save(existingOrg);
            })
            .map(organizationMapper::toDTO)
            .doOnSuccess(dto -> log.info("Updated organization with ID: {}", dto.getOrganizationId()))
            .doOnError(e -> log.error("Error updating organization ID: {}", id, e));
    }

    @Transactional
    public Mono<Void> deleteOrganization(UUID id) {
        log.info("Deleting organization by ID: {}", id);
        return organizationRepository.deleteById(id)
            .doOnSuccess(v -> log.info("Deleted organization with ID: {}", id))
            .doOnError(e -> log.error("Error deleting organization ID: {}", id, e));
    }
}
