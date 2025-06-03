package com.poi.yow_point.controllers;

import com.poi.yow_point.dto.PoiDTO;
//import com.poi.yow_point.services.PoiService;
import com.poi.yow_point.interfaces.PoiInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pois")
@Tag(name = "Points of Interest API", description = "Operations pertaining to Points of Interest")
public class PoiController {

    private static final Logger logger = LoggerFactory.getLogger(PoiController.class);
    private final PoiInterface poiService;

    @Autowired
    public PoiController(PoiInterface poiService) {
        this.poiService = poiService;
    }

    @Operation(summary = "Create a new Point of Interest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "PoI created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PoiDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<PoiDTO> createPoi(@Valid @RequestBody PoiDTO poiDTO) {
        logger.info("Received request to create PoI: {}", poiDTO.getName());
        PoiDTO createdPoi = poiService.createPoi(poiDTO);
        return new ResponseEntity<>(createdPoi, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a PoI by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PoI found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PoiDTO.class))),
            @ApiResponse(responseCode = "404", description = "PoI not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PoiDTO> getPoiById(
            @Parameter(description = "ID of the PoI to be retrieved", required = true) @PathVariable UUID id) {
        logger.debug("Received request to get PoI by ID: {}", id);
        return poiService.getPoiById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("PoI with ID {} not found for GET request.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Get all Points of Interest or filter by country and/or city")
    @GetMapping
    public ResponseEntity<List<PoiDTO>> getAllPois(
            @Parameter(description = "Filter by country (case-insensitive)") @RequestParam(required = false) String country,
            @Parameter(description = "Filter by city (case-insensitive)") @RequestParam(required = false) String city) {
        List<PoiDTO> pois;
        if (country != null && city != null) {
            logger.info("Fetching PoIs for country: {} and city: {}", country, city);
            pois = poiService.findPoisByCountryAndCity(country, city);
        } else if (country != null) {
            logger.info("Fetching PoIs for country: {}", country);
            pois = poiService.findPoisByCountry(country);
        } else if (city != null) {
            logger.info("Fetching PoIs for city: {}", city);
            pois = poiService.findPoisByCity(city);
        } else {
            logger.info("Fetching all PoIs");
            pois = poiService.getAllPois();
        }
        return ResponseEntity.ok(pois);
    }

    @Operation(summary = "Update an existing Point of Interest")
    // ... (ApiResponses comme avant)
    @PutMapping("/{id}")
    public ResponseEntity<PoiDTO> updatePoi(
            @Parameter(description = "ID of the PoI to be updated", required = true) @PathVariable UUID id,
            @Valid @RequestBody PoiDTO poiDTO) {
        logger.info("Received request to update PoI with ID: {}", id);
        return poiService.updatePoi(id, poiDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("PoI with ID {} not found for PUT request.", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @Operation(summary = "Delete a Point of Interest by its ID")
    // ... (ApiResponses comme avant)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoi(
            @Parameter(description = "ID of the PoI to be deleted", required = true) @PathVariable UUID id) {
        logger.info("Received request to delete PoI with ID: {}", id);
        if (poiService.deletePoi(id)) {
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("PoI with ID {} not found for DELETE request.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Find PoIs by category")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<PoiDTO>> getPoisByCategory(
            @Parameter(description = "Category to search for", required = true) @PathVariable String category) {
        logger.info("Received request to find PoIs by category: {}", category);
        List<PoiDTO> pois = poiService.findPoisByCategory(category);
        return ResponseEntity.ok(pois);
    }

    @Operation(summary = "Find PoIs by name containing a fragment")
    @GetMapping("/search")
    public ResponseEntity<List<PoiDTO>> searchPoisByName(
            @Parameter(description = "Name fragment to search for", required = true) @RequestParam String name) {
        logger.info("Received request to search PoIs by name: {}", name);
        List<PoiDTO> pois = poiService.findPoisByNameContaining(name);
        return ResponseEntity.ok(pois);
    }

    @Operation(summary = "Find PoIs nearby a given location")
    // ... (ApiResponses et Parameters comme avant)
    @GetMapping("/nearby")
    public ResponseEntity<List<PoiDTO>> getNearbyPois(
            @Parameter(description = "Latitude of the center point for search", required = true, example = "4.0511") @RequestParam double lat,
            @Parameter(description = "Longitude of the center point for search", required = true, example = "11.5200") @RequestParam double lon,
            @Parameter(description = "Search radius in meters", required = false, schema = @Schema(type = "number", format = "double", defaultValue = "1000")) @RequestParam(defaultValue = "1000") double distanceMeters) {
        logger.info("Received request to find nearby PoIs for lat={}, lon={}, distance={}m", lat, lon, distanceMeters);
        List<PoiDTO> pois = poiService.findNearbyPois(lat, lon, distanceMeters);
        return ResponseEntity.ok(pois);
    }
}