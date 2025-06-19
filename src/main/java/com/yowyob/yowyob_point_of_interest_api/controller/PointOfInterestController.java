package com.yowyob.yowyob_point_of_interest_api.controller;

import com.yowyob.yowyob_point_of_interest_api.dto.PointOfInterestDTO;
import com.yowyob.yowyob_point_of_interest_api.service.PointOfInterestService;
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

// import java.util.List; // Will be replaced by Flux
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
    public Mono<ResponseEntity<PointOfInterestDTO>> createPoi(@RequestBody PointOfInterestDTO poiDTO) {
        log.info("Received reactive request to create POI: {}", poiDTO.getPoiName());
        return pointOfInterestService.savePoi(poiDTO)
            .map(savedPoi -> new ResponseEntity<>(savedPoi, HttpStatus.CREATED))
            .doOnSuccess(response -> {
                if (response.getBody() != null) {
                    log.info("POI created with ID: {}", response.getBody().getPoiId());
                }
            })
            .doOnError(e -> log.error("Error creating POI: {}", poiDTO.getPoiName(), e));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Point of Interest by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the POI",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "POI not found")
    })
    public Mono<ResponseEntity<PointOfInterestDTO>> getPoiById(@Parameter(description = "ID of the POI") @PathVariable UUID id) {
        log.info("Received reactive request to get POI by ID: {}", id);
        return pointOfInterestService.getPoiById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build())
            .doOnError(e -> log.error("Error getting POI ID: {}", id, e));
    }

    @GetMapping
    @Operation(summary = "Get all Points of Interest, with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of POIs",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    })
    public Flux<PointOfInterestDTO> getAllPois(
            @Parameter(description = "Filter by POI name") @RequestParam(required = false) String name,
            @Parameter(description = "Filter by POI type") @RequestParam(required = false) String type,
            @Parameter(description = "Filter by POI category") @RequestParam(required = false) String category) {
        log.info("Received reactive request to get all POIs with filters - name: [{}], type: [{}], category: [{}]", name, type, category);
        Flux<PointOfInterestDTO> poisFlux;
        if (name != null) {
            poisFlux = pointOfInterestService.findPoisByName(name);
        } else if (type != null) {
            poisFlux = pointOfInterestService.findPoisByType(type);
        } else if (category != null) {
            poisFlux = pointOfInterestService.findPoisByCategory(category);
        } else {
            poisFlux = pointOfInterestService.getAllPois();
        }
        return poisFlux
            .doOnComplete(() -> log.info("Finished streaming POIs for the request."))
            .doOnError(e -> log.error("Error streaming POIs", e));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Point of Interest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POI updated successfully",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class))),
            @ApiResponse(responseCode = "404", description = "POI not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Mono<ResponseEntity<PointOfInterestDTO>> updatePoi(
            @Parameter(description = "ID of the POI to be updated") @PathVariable UUID id,
            @RequestBody PointOfInterestDTO poiDetailsDTO) {
        log.info("Received reactive request to update POI with ID: {}", id);
        return pointOfInterestService.updatePoi(id, poiDetailsDTO)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build()) // If service returns empty Mono for not found
            .doOnSuccess(response -> {
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    log.info("POI updated with ID: {}", response.getBody().getPoiId());
                } else if (response.getStatusCode() == HttpStatus.NOT_FOUND){
                    log.warn("POI not found for update, ID: {}", id);
                }
            })
            .doOnError(e -> log.error("Error updating POI ID: {}", id, e));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Point of Interest by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "POI deleted successfully"),
            @ApiResponse(responseCode = "404", description = "POI not found")
    })
    public Mono<ResponseEntity<Void>> deletePoi(@Parameter(description = "ID of the POI to be deleted") @PathVariable UUID id) {
        log.info("Received reactive request to delete POI by ID: {}", id);
        return pointOfInterestService.deletePoi(id)
            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
            .doOnSuccess(response -> log.info("POI deleted with ID: {}", id))
            .doOnError(e -> log.error("Error deleting POI ID: {}", id, e));
    }

    @GetMapping("/nearby")
    @Operation(summary = "Find Points of Interest within a certain distance from a location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of nearby POIs",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    })
    public Flux<PointOfInterestDTO> findNearbyPois( // Return Flux directly
            @Parameter(description = "Longitude", required = true) @RequestParam double longitude,
            @Parameter(description = "Latitude", required = true) @RequestParam double latitude,
            @Parameter(description = "Distance in meters", required = true) @RequestParam double distance) {
        log.info("Received reactive request for nearby POIs. Long: {}, Lat: {}, Dist: {}", longitude, latitude, distance);
        return pointOfInterestService.findPoisNearby(longitude, latitude, distance)
            .doOnComplete(() -> log.info("Finished streaming nearby POIs."))
            .doOnError(e -> log.error("Error finding nearby POIs", e));
    }

    @GetMapping("/town/{townId}")
    @Operation(summary = "Get Points of Interest by Town ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved POIs for town",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    })
    public Flux<PointOfInterestDTO> getPoisByTownId( // Return Flux directly
            @Parameter(description = "UUID of the town") @PathVariable UUID townId) {
        log.info("Received reactive request for POIs by town ID: {}", townId);
        return pointOfInterestService.findPoisByTownId(townId)
            .doOnComplete(() -> log.info("Finished streaming POIs for town ID: {}", townId))
            .doOnError(e -> log.error("Error getting POIs for town ID: {}", townId, e));
    }

    @GetMapping("/state/{stateProvince}")
    @Operation(summary = "Get Points of Interest by State/Province")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved POIs for state/province",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    })
    public Flux<PointOfInterestDTO> getPoisByStateProvince(
            @Parameter(description = "Name of state/province") @PathVariable String stateProvince) {
        log.info("Received reactive request for POIs by state/province: {}", stateProvince);
        return pointOfInterestService.findPoisByStateProvince(stateProvince)
            .doOnComplete(() -> log.info("Finished streaming POIs for state: {}", stateProvince))
            .doOnError(e -> log.error("Error getting POIs for state: {}", stateProvince, e));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Points of Interest created by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved POIs for user",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    })
    public Flux<PointOfInterestDTO> getPoisByCreatorUserId(
            @Parameter(description = "UUID of the creator user") @PathVariable UUID userId) {
        log.info("Received reactive request for POIs by creator user ID: {}", userId);
        return pointOfInterestService.findPoisByCreatorUserId(userId)
            .doOnComplete(() -> log.info("Finished streaming POIs for user ID: {}", userId))
            .doOnError(e -> log.error("Error getting POIs for user ID: {}", userId, e));
    }

    @GetMapping("/popular")
    @Operation(summary = "Get all Points of Interest ordered by popularity score (descending)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved POIs ordered by popularity",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    })
    public Flux<PointOfInterestDTO> getPoisByPopularity() {
        log.info("Received reactive request for POIs ordered by popularity");
        return pointOfInterestService.findPoisOrderByPopularityScoreDesc()
            .doOnComplete(() -> log.info("Finished streaming popular POIs."))
            .doOnError(e -> log.error("Error getting popular POIs", e));
    }

    @GetMapping("/active/popular")
    @Operation(summary = "Get active Points of Interest ordered by popularity score (descending)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved active POIs ordered by popularity",
                         content = @Content(schema = @Schema(implementation = PointOfInterestDTO.class)))
    })
    public Flux<PointOfInterestDTO> getActivePoisByPopularity() {
        log.info("Received reactive request for active POIs ordered by popularity");
        return pointOfInterestService.findActivePoisOrderByPopularityScoreDesc()
            .doOnComplete(() -> log.info("Finished streaming active popular POIs."))
            .doOnError(e -> log.error("Error getting active popular POIs", e));
    }
}
