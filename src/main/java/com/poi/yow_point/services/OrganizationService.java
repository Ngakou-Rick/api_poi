package com.poi.yow_point.services;

import com.poi.yow_point.dto.OrganizationDTO;
import com.poi.yow_point.mappers.OrganizationMapper;
import com.poi.yow_point.models.Organization;
import com.poi.yow_point.repositories.OrganizationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        return Mono.fromCallable(() -> {
            log.info("Saving organization: {}", organizationDTO.getOrgName());
            return organizationMapper.toEntity(organizationDTO);
        })
                .flatMap(organization -> organizationRepository.save(organization))
                .doOnNext(savedOrg -> log.info("Saved organization with ID: {}", savedOrg.getOrganizationId()))
                .map(organizationMapper::toDTO)
                .doOnError(error -> log.error("Error saving organization: {}", error.getMessage()));
    }

    public Mono<OrganizationDTO> getOrganizationById(UUID id) {
        log.info("Fetching organization by ID: {}", id);
        return organizationRepository.findById(id)
                .doOnNext(org -> log.info("Found organization: {}", org.getOrgName()))
                .map(organizationMapper::toDTO)
                .doOnError(error -> log.error("Error fetching organization by ID {}: {}", id, error.getMessage()));
    }

    public Flux<OrganizationDTO> getAllOrganizations() {
        log.info("Fetching all organizations");
        return organizationRepository.findAll()
                .map(organizationMapper::toDTO)
                .doOnComplete(() -> log.info("Completed fetching all organizations"))
                .doOnError(error -> log.error("Error fetching all organizations: {}", error.getMessage()));
    }

    @Transactional
    public Mono<OrganizationDTO> updateOrganization(UUID id, OrganizationDTO organizationDTO) {
        log.info("Updating organization with ID: {}", id);
        return organizationRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Organization not found with id " + id)))
                .map(existingOrg -> {
                    // Mise à jour des champs depuis le DTO
                    existingOrg.setOrgName(organizationDTO.getOrgName());
                    existingOrg.setOrgCode(organizationDTO.getOrgCode());
                    existingOrg.setOrgType(organizationDTO.getOrgType());
                    existingOrg.setIsActive(organizationDTO.getIsActive());
                    // createdAt n'est généralement pas mis à jour
                    return existingOrg;
                })
                .flatMap(organizationRepository::save)
                .doOnNext(updatedOrg -> log.info("Updated organization with ID: {}", updatedOrg.getOrganizationId()))
                .map(organizationMapper::toDTO)
                .doOnError(error -> log.error("Error updating organization with ID {}: {}", id, error.getMessage()));
    }

    @Transactional
    public Mono<Void> deleteOrganization(UUID id) {
        log.info("Deleting organization by ID: {}", id);
        return organizationRepository.deleteById(id)
                .doOnSuccess(unused -> log.info("Deleted organization with ID: {}", id))
                .doOnError(error -> log.error("Error deleting organization with ID {}: {}", id, error.getMessage()));
    }

    // Méthode utilitaire pour rechercher par orgCode
    public Mono<OrganizationDTO> getOrganizationByOrgCode(String orgCode) {
        log.info("Fetching organization by orgCode: {}", orgCode);
        return organizationRepository.findByOrgCode(orgCode)
                .map(organizationMapper::toDTO)
                .doOnError(error -> log.error("Error fetching organization by orgCode {}: {}", orgCode,
                        error.getMessage()));
    }
}