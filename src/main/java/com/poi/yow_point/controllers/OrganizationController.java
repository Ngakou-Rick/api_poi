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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.poi.yow_point.dto.OrganizationDTO;
import com.poi.yow_point.services.OrganizationService;

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
            @ApiResponse(responseCode = "201", description = "Organization created successfully", content = @Content(schema = @Schema(implementation = OrganizationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<ResponseEntity<OrganizationDTO>> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        log.info("Received request to create organization: {}", organizationDTO.getOrgName());
        return organizationService.saveOrganization(organizationDTO)
                .map(savedOrganization -> {
                    log.info("Organization created with ID: {}", savedOrganization.getOrganizationId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedOrganization);
                })
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an organization by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the organization", content = @Content(schema = @Schema(implementation = OrganizationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public Mono<ResponseEntity<OrganizationDTO>> getOrganizationById(
            @Parameter(description = "ID of the organization to be retrieved") @PathVariable UUID id) {
        log.info("Received request to get organization by ID: {}", id);
        return organizationService.getOrganizationById(id)
                .map(organization -> {
                    log.info("Found organization: {}", organization.getOrgName());
                    return ResponseEntity.ok(organization);
                })
                .switchIfEmpty(Mono.fromSupplier(() -> {
                    log.warn("Organization not found for ID: {}", id);
                    return ResponseEntity.notFound().build();
                }));
    }

    @GetMapping
    @Operation(summary = "Get all organizations")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of organizations", content = @Content(schema = @Schema(implementation = OrganizationDTO.class)))
    public Flux<OrganizationDTO> getAllOrganizations() {
        log.info("Received request to get all organizations");
        return organizationService.getAllOrganizations()
                .doOnComplete(() -> log.info("Completed streaming all organizations"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing organization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization updated successfully", content = @Content(schema = @Schema(implementation = OrganizationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<ResponseEntity<OrganizationDTO>> updateOrganization(
            @Parameter(description = "ID of the organization to be updated") @PathVariable UUID id,
            @RequestBody OrganizationDTO organizationDTO) {
        log.info("Received request to update organization with ID: {}", id);
        return organizationService.updateOrganization(id, organizationDTO)
                .map(updatedOrganization -> {
                    log.info("Organization updated with ID: {}", updatedOrganization.getOrganizationId());
                    return ResponseEntity.ok(updatedOrganization);
                })
                .onErrorReturn(RuntimeException.class, ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an organization by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organization deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public Mono<ResponseEntity<Void>> deleteOrganization(
            @Parameter(description = "ID of the organization to be deleted") @PathVariable UUID id) {
        log.info("Received request to delete organization by ID: {}", id);
        return organizationService.deleteOrganization(id)
                .then(Mono.fromSupplier(() -> {
                    log.info("Organization deleted with ID: {}", id);
                    return ResponseEntity.noContent().<Void>build();
                }))
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    // Endpoint bonus pour rechercher par orgCode
    @GetMapping("/by-code/{orgCode}")
    @Operation(summary = "Get an organization by its organization code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the organization", content = @Content(schema = @Schema(implementation = OrganizationDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization not found")
    })
    public Mono<ResponseEntity<OrganizationDTO>> getOrganizationByOrgCode(
            @Parameter(description = "Organization code") @PathVariable String orgCode) {
        log.info("Received request to get organization by orgCode: {}", orgCode);
        return organizationService.getOrganizationByOrgCode(orgCode)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromSupplier(() -> {
                    log.warn("Organization not found for orgCode: {}", orgCode);
                    return ResponseEntity.notFound().build();
                }));
    }
}