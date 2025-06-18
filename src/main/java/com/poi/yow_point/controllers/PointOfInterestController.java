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

import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.services.PointOfInterestService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pois")
@Tag(name = "Point of Interest API", description = "APIs for managing Points of Interest (POIs)")
public class PointOfInterestController {

    private static final Logger log = LoggerFactory.getLogger(PointOfInterestController.class);
    private final PointOfInterestService pointOfInterestService;

    @Autowired
    public PointOfInterestController(PointOfInterestService pointOfInterestService) {
        this.pointOfInterestService = pointOfInterestService;
    }

    @PostMapping
    @Operation(summary = "Create a new Point of Interest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "POI created successfully",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<PointOfInterestDTO> createPoi(@RequestBody PointOfInterestDTO poiDTO) {
        log.info("Received request to create POI: {}", poiDTO.getPoiName());
        PointOfInterestDTO savedPoi = pointOfInterestService.savePoi(poiDTO);
        log.info("POI created with ID: {}", savedPoi.getPoiId());
        return new ResponseEntity<>(savedPoi, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Point of Interest by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the POI",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "POI not found")
    })
    public ResponseEntity<PointOfInterestDTO> getPoiById(@Parameter(description = "ID of the POI to be retrieved") @PathVariable UUID id) {
        log.info("Received request to get POI by ID: {}", id);
        return pointOfInterestService.getPoiById(id)
                .map(poi -> {
                    log.info("Found POI: {}", poi.getPoiName());
                    return ResponseEntity.ok(poi);
                })
                .orElseGet(() -> {
                    log.warn("POI not found for ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    @Operation(summary = "Get all Points of Interest, with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of POIs",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    })
    public ResponseEntity<List<PointOfInterestDTO>> getAllPois(
            @Parameter(description = "Filter by POI name (case-insensitive, partial match)") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by POI type") @RequestParam(required = false) String type,
            @Parameter(description = "Filter by POI category") @RequestParam(required = false) String category) {
        log.info("Received request to get all POIs with filters - name: [{}], type: [{}], category: [{}]", name, type, category);
        List<PointOfInterestDTO> pois;
        if (name != null) {
            pois = pointOfInterestService.findPoisByName(name);
        } else if (type != null) {
            pois = pointOfInterestService.findPoisByType(type);
        } else if (category != null) {
            pois = pointOfInterestService.findPoisByCategory(category);
        } else {
            pois = pointOfInterestService.getAllPois();
        }
        log.info("Returning {} POIs", pois.size());
        return ResponseEntity.ok(pois);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Point of Interest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POI updated successfully",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "POI not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<PointOfInterestDTO> updatePoi(
            @Parameter(description = "ID of the POI to be updated") @PathVariable UUID id, 
            @RequestBody PointOfInterestDTO poiDetailsDTO) {
        log.info("Received request to update POI with ID: {}", id);
        try {
            PointOfInterestDTO updatedPoi = pointOfInterestService.updatePoi(id, poiDetailsDTO);
            log.info("POI updated with ID: {}", updatedPoi.getPoiId());
            return ResponseEntity.ok(updatedPoi);
        } catch (RuntimeException e) { 
            log.warn("Failed to update POI with ID: {}. Reason: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Point of Interest by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "POI deleted successfully"),
            @ApiResponse(responseCode = "404", description = "POI not found")
    })
    public ResponseEntity<Void> deletePoi(@Parameter(description = "ID of the POI to be deleted") @PathVariable UUID id) {
        log.info("Received request to delete POI by ID: {}", id);
        pointOfInterestService.deletePoi(id);
        log.info("POI deleted with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/nearby")
    @Operation(summary = "Find Points of Interest within a certain distance from a location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of nearby POIs",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input for location or distance")
    })
    public ResponseEntity<List<PointOfInterestDTO>> findNearbyPois(
            @Parameter(description = "Longitude of the center point for search", required = true) @RequestParam double longitude,
            @Parameter(description = "Latitude of the center point for search", required = true) @RequestParam double latitude,
            @Parameter(description = "Search radius in meters", required = true) @RequestParam double distance) {
        log.info("Received request to find nearby POIs. Longitude: {}, Latitude: {}, Distance: {}", longitude, latitude, distance);
        try {
            List<PointOfInterestDTO> pois = pointOfInterestService.findPoisNearby(longitude, latitude, distance);
            log.info("Found {} POIs nearby.", pois.size());
            return ResponseEntity.ok(pois);
        } catch (Exception e) {
            log.error("Error finding nearby POIs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); 
        }
    }

    @GetMapping("/town/{townId}")
    @Operation(summary = "Get Points of Interest by Town ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved POIs for the town",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "No POIs found for this town ID or invalid ID") 
    })
    public ResponseEntity<List<PointOfInterestDTO>> getPoisByTownId(
            @Parameter(description = "UUID of the town", required = true) @PathVariable UUID townId) {
        log.info("Received request to get POIs by town ID: {}", townId);
        List<PointOfInterestDTO> pois = pointOfInterestService.findPoisByTownId(townId);
        if (pois.isEmpty()) {
            log.info("No POIs found for town ID: {}", townId);
            return ResponseEntity.notFound().build();
        }
        log.info("Returning {} POIs for town ID: {}", pois.size(), townId);
        return ResponseEntity.ok(pois);
    }

    @GetMapping("/state/{stateProvince}")
    @Operation(summary = "Get Points of Interest by State/Province")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved POIs for the state/province",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class))),
    })
    public ResponseEntity<List<PointOfInterestDTO>> getPoisByStateProvince(
            @Parameter(description = "Name of the state or province (case-insensitive)", required = true) @PathVariable String stateProvince) {
        log.info("Received request to get POIs by state/province: {}", stateProvince);
        List<PointOfInterestDTO> pois = pointOfInterestService.findPoisByStateProvince(stateProvince);
        log.info("Returning {} POIs for state/province: {}", pois.size(), stateProvince);
        return ResponseEntity.ok(pois);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Points of Interest created by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved POIs for the user",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "No POIs found for this user ID or invalid ID")
    })
    public ResponseEntity<List<PointOfInterestDTO>> getPoisByCreatorUserId(
            @Parameter(description = "UUID of the user who created the POIs", required = true) @PathVariable UUID userId) {
        log.info("Received request to get POIs by creator user ID: {}", userId);
        List<PointOfInterestDTO> pois = pointOfInterestService.findPoisByCreatorUserId(userId);
         if (pois.isEmpty()) {
            log.info("No POIs found for creator user ID: {}", userId);
            return ResponseEntity.notFound().build(); 
        }
        log.info("Returning {} POIs for creator user ID: {}", pois.size(), userId);
        return ResponseEntity.ok(pois);
    }

    @GetMapping("/popular")
    @Operation(summary = "Get all Points of Interest ordered by popularity score (descending)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved POIs ordered by popularity",
                 content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    public ResponseEntity<List<PointOfInterestDTO>> getPoisByPopularity() {
        log.info("Received request to get POIs ordered by popularity");
        List<PointOfInterestDTO> pois = pointOfInterestService.findPoisOrderByPopularityScoreDesc();
        log.info("Returning {} POIs ordered by popularity", pois.size());
        return ResponseEntity.ok(pois);
    }
    
    @GetMapping("/active/popular")
    @Operation(summary = "Get active Points of Interest ordered by popularity score (descending)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active POIs ordered by popularity",
                 content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    public ResponseEntity<List<PointOfInterestDTO>> getActivePoisByPopularity() {
        log.info("Received request to get active POIs ordered by popularity");
        List<PointOfInterestDTO> pois = pointOfInterestService.findActivePoisOrderByPopularityScoreDesc();
        log.info("Returning {} active POIs ordered by popularity", pois.size());
        return ResponseEntity.ok(pois);
    }
}
