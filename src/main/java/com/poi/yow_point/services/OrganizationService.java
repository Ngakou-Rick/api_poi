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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public OrganizationDTO saveOrganization(OrganizationDTO organizationDTO) {
        log.info("Saving organization: {}", organizationDTO.getOrgName());
        Organization organization = organizationMapper.toEntity(organizationDTO);
        // If ID is null, it's a create operation. If ID is present, it's an update.
        // For updates, ensure to handle existing entities correctly if needed (e.g. merging)
        Organization savedOrg = organizationRepository.save(organization);
        log.info("Saved organization with ID: {}", savedOrg.getOrganizationId());
        return organizationMapper.toDTO(savedOrg);
    }

    public Optional<OrganizationDTO> getOrganizationById(UUID id) {
        log.info("Fetching organization by ID: {}", id);
        return organizationRepository.findById(id)
                                     .map(organizationMapper::toDTO);
    }

    public List<OrganizationDTO> getAllOrganizations() {
        log.info("Fetching all organizations");
        return organizationRepository.findAll()
                                     .stream()
                                     .map(organizationMapper::toDTO)
                                     .collect(Collectors.toList());
    }
    
    @Transactional
    public OrganizationDTO updateOrganization(UUID id, OrganizationDTO organizationDTO) {
        log.info("Updating organization with ID: {}", id);
        Organization existingOrg = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id " + id)); // TODO: Custom exception

        // Update fields from DTO
        existingOrg.setOrgName(organizationDTO.getOrgName());
        existingOrg.setOrgCode(organizationDTO.getOrgCode());
        existingOrg.setOrgType(organizationDTO.getOrgType());
        existingOrg.setActive(organizationDTO.getIsActive());
        // createdAt is typically not updated

        Organization updatedOrg = organizationRepository.save(existingOrg);
        log.info("Updated organization with ID: {}", updatedOrg.getOrganizationId());
        return organizationMapper.toDTO(updatedOrg);
    }

    @Transactional
    public void deleteOrganization(UUID id) {
        log.info("Deleting organization by ID: {}", id);
        organizationRepository.deleteById(id);
        log.info("Deleted organization with ID: {}", id);
    }
}