package com.poi.yow_point.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poi.yow_point.dto.OrganizationDTO;
import com.poi.yow_point.services.OrganizationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
@Tag(name = "Organization API", description = "APIs for managing organizations")
public class OrganizationController {

    private static final Logger log = LoggerFactory.getLogger(OrganizationController.class);
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    @Operation(summary = "Create a new organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Organization created successfully",
                         content = @Content(schema = @Schema(implementation = OrganizationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        log.info("Received request to create organization: {}", organizationDTO.getOrgName());
        OrganizationDTO savedOrganization = organizationService.saveOrganization(organizationDTO);
        log.info("Organization created with ID: {}", savedOrganization.getOrganizationId());
        return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an organization by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the organization",
                         content = @Content(schema = @Schema(implementation = OrganizationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public ResponseEntity<OrganizationDTO> getOrganizationById(@Parameter(description = "ID of the organization to be retrieved") @PathVariable UUID id) {
        log.info("Received request to get organization by ID: {}", id);
        return organizationService.getOrganizationById(id)
                .map(org -> {
                    log.info("Found organization: {}", org.getOrgName());
                    return ResponseEntity.ok(org);
                })
                .orElseGet(() -> {
                    log.warn("Organization not found for ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    @Operation(summary = "Get all organizations")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of organizations",
                 content = @Content(schema = @Schema(implementation = OrganizationDTO.class)))
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
        log.info("Received request to get all organizations");
        List<OrganizationDTO> organizations = organizationService.getAllOrganizations();
        log.info("Returning {} organizations", organizations.size());
        return ResponseEntity.ok(organizations);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization updated successfully",
                         content = @Content(schema = @Schema(implementation = OrganizationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<OrganizationDTO> updateOrganization(
            @Parameter(description = "ID of the organization to be updated") @PathVariable UUID id,
            @RequestBody OrganizationDTO organizationDTO) {
        log.info("Received request to update organization with ID: {}", id);
        try {
            OrganizationDTO updatedOrganization = organizationService.updateOrganization(id, organizationDTO);
            log.info("Organization updated with ID: {}", updatedOrganization.getOrganizationId());
            return ResponseEntity.ok(updatedOrganization);
        } catch (RuntimeException e){ // TODO: Specific exception
             log.warn("Organization not found for update, ID: {}. Reason: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an organization by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organization deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public ResponseEntity<Void> deleteOrganization(@Parameter(description = "ID of the organization to be deleted") @PathVariable UUID id) {
        log.info("Received request to delete organization by ID: {}", id);
        organizationService.deleteOrganization(id);
        log.info("Organization deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
