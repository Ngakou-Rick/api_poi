package com.yowyob.yowyob_point_of_interest_api.service;

import com.yowyob.yowyob_point_of_interest_api.model.Organization;
import com.yowyob.yowyob_point_of_interest_api.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization saveOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Optional<Organization> getOrganizationById(UUID id) {
        return organizationRepository.findById(id);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public void deleteOrganization(UUID id) {
        organizationRepository.deleteById(id);
    }

    // Add other business logic methods as needed
}
