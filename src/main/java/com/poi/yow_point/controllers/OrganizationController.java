package com.poi.yow_point.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poi.yow_point.models.Organization;
import com.poi.yow_point.services.OrganizationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations") // Base path for organization-related APIs
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        Organization savedOrganization = organizationService.saveOrganization(organization);
        return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable UUID id) {
        return organizationService.getOrganizationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable UUID id, @RequestBody Organization organizationDetails) {
        // Assuming OrganizationService will have an update method similar to PointOfInterestService
        // For now, just saving, which would overwrite or create if ID is not managed by JPA properly for updates
        // A proper update method would fetch existing, update fields, then save.
        Organization organization = organizationService.getOrganizationById(id)
            .orElse(null); // Or throw exception

        if (organization == null) {
            return ResponseEntity.notFound().build();
        }
        organization.setOrgName(organizationDetails.getOrgName());
        organization.setOrgCode(organizationDetails.getOrgCode());
        organization.setOrgType(organizationDetails.getOrgType());
        organization.setActive(organizationDetails.isActive());
        
        Organization updatedOrganization = organizationService.saveOrganization(organization);
        return ResponseEntity.ok(updatedOrganization);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable UUID id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}
