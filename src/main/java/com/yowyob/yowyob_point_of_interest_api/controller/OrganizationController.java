package com.yowyob.yowyob_point_of_interest_api.controller;

import com.yowyob.yowyob_point_of_interest_api.dto.OrganizationDTO;
import com.yowyob.yowyob_point_of_interest_api.service.OrganizationService;
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
import reactor.core.publisher.Flux; // Added
import reactor.core.publisher.Mono; // Added

// import java.util.List; // Keep for getAllOrganizations if service returns List for now, but should be Flux - Removed as Flux is used
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
    public Mono<ResponseEntity<OrganizationDTO>> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        log.info("Received reactive request to create organization: {}", organizationDTO.getOrgName());
        return organizationService.saveOrganization(organizationDTO)
            .map(savedOrg -> new ResponseEntity<>(savedOrg, HttpStatus.CREATED))
            .doOnSuccess(response -> {
                if(response.getBody() != null) { // Ensure body is not null before accessing
                    log.info("Organization created with ID: {}", response.getBody().getOrganizationId());
                }
            })
            .doOnError(e -> log.error("Error creating organization: {}", organizationDTO.getOrgName(), e));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an organization by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the organization",
                         content = @Content(schema = @Schema(implementation = OrganizationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public Mono<ResponseEntity<OrganizationDTO>> getOrganizationById(@Parameter(description = "ID of the organization") @PathVariable UUID id) {
        log.info("Received reactive request to get organization by ID: {}", id);
        return organizationService.getOrganizationById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .doOnError(e -> log.error("Error getting organization ID: {}", id, e));
    }

    @GetMapping
    @Operation(summary = "Get all organizations")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of organizations",
                 content = @Content(schema = @Schema(implementation = OrganizationDTO.class))) // Schema for individual items in Flux
    public Flux<OrganizationDTO> getAllOrganizations() { // Return Flux directly
        log.info("Received reactive request to get all organizations");
        return organizationService.getAllOrganizations()
                 .doOnComplete(() -> log.info("Finished streaming all organizations."))
                 .doOnError(e -> log.error("Error getting all organizations", e));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization updated successfully",
                         content = @Content(schema = @Schema(implementation = OrganizationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<ResponseEntity<OrganizationDTO>> updateOrganization(
            @Parameter(description = "ID of the organization to be updated") @PathVariable UUID id,
            @RequestBody OrganizationDTO organizationDTO) {
        log.info("Received reactive request to update organization with ID: {}", id);
        return organizationService.updateOrganization(id, organizationDTO)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build()) // If updateOrganization returns empty Mono on not found
            .doOnSuccess(response -> {
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    log.info("Organization updated with ID: {}", response.getBody().getOrganizationId());
                } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                    log.warn("Organization not found for update, ID: {}", id);
                }
            })
            .doOnError(e -> log.error("Error updating organization ID: {}", id, e));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an organization by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organization deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Organization not found") // Though delete might not return 404 if item doesn't exist
    })
    public Mono<ResponseEntity<Void>> deleteOrganization(@Parameter(description = "ID of organization to be deleted") @PathVariable UUID id) {
        log.info("Received reactive request to delete organization by ID: {}", id);
        return organizationService.deleteOrganization(id)
            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
            .doOnSuccess(response -> log.info("Organization deleted with ID: {}", id))
            .doOnError(e -> log.error("Error deleting organization ID: {}", id, e));
    }
}
