package com.yowyob.yowyob_point_of_interest_api.service;

import com.fasterxml.jackson.core.JsonProcessingException; // Added
import com.fasterxml.jackson.core.type.TypeReference; // Added
import com.fasterxml.jackson.databind.ObjectMapper; // Added
import com.yowyob.yowyob_point_of_interest_api.dto.ContactPersonTypeDTO; // Added
import com.yowyob.yowyob_point_of_interest_api.dto.PointOfInterestDTO;
import com.yowyob.yowyob_point_of_interest_api.mapper.PointOfInterestMapper;
import com.yowyob.yowyob_point_of_interest_api.model.PointOfInterest;
// AppUserRepository and OrganizationRepository are not directly needed if POI entity only stores IDs
// and DTO provides these IDs, which are then mapped to the entity by PointOfInterestMapper or set directly.
// However, if we needed to validate existence of orgId or userId, we'd inject their repositories.
// For now, assuming IDs from DTO are valid and mapper handles direct ID fields.

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTWriter; // To create WKT for repository query
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.yowyob.yowyob_point_of_interest_api.repository.PointOfInterestRepository;


import java.time.OffsetDateTime;
import java.util.Collections; // Added
import java.util.List;
import java.util.UUID;
// Removed .stream().map().collect() as Flux/Mono handle mapping

@Service
public class PointOfInterestService {

    private static final Logger log = LoggerFactory.getLogger(PointOfInterestService.class);
    private final PointOfInterestRepository pointOfInterestRepository;
    private final PointOfInterestMapper poiMapper;
    private final ObjectMapper objectMapper; // For JSON serialization/deserialization
    private final GeometryFactory geometryFactory; // For creating JTS Point
    private final WKTWriter wktWriter; // For creating WKT string from JTS Point

    @Autowired
    public PointOfInterestService(PointOfInterestRepository pointOfInterestRepository,
                                  PointOfInterestMapper poiMapper,
                                  ObjectMapper objectMapper) {
        this.pointOfInterestRepository = pointOfInterestRepository;
        this.poiMapper = poiMapper;
        this.objectMapper = objectMapper;
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326); // SRID 4326 for WGS 84
        this.wktWriter = new WKTWriter();
    }

    private String convertContactsToJson(List<ContactPersonTypeDTO> contacts) {
        if (contacts == null || contacts.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(contacts);
        } catch (JsonProcessingException e) {
            log.error("Error serializing contacts to JSON: {}", e.getMessage());
            throw new RuntimeException("Error serializing contacts", e); // Or handle more gracefully
        }
    }

    // Note: Deserialization for poiContactsJson from entity to List<ContactPersonTypeDTO> in DTO
    // will be handled by the PointOfInterestMapper if it's configured for such custom mapping,
    // or it can be handled here if mapper passes String through.
    // The current PointOfInterestMapper doesn't handle this JSON string to List<ContactPersonTypeDTO> directly.
    // This will be addressed when refactoring the mapper in conjunction with DTOs.
    // For now, assume mapper or DTO handles it. Let's adjust mapper for this.

    @Transactional
    public Mono<PointOfInterestDTO> savePoi(PointOfInterestDTO poiDTO) {
        log.info("Saving POI: {}", poiDTO.getPoiName());
        PointOfInterest poi = poiMapper.toEntity(poiDTO); // This mapper needs to handle DTO.lat/lon to entity.locationGeogWkt

        if (poi.getPoiId() == null) { // New POI
            poi.setPoiId(UUID.randomUUID());
            poi.setCreatedAt(OffsetDateTime.now());
            if (poi.getIsActive() == null) {
                poi.setIsActive(true);
            }
            if (poi.getPopularityScore() == null) {
                poi.setPopularityScore(0.0f);
            }
        }
        poi.setUpdatedAt(OffsetDateTime.now()); // Set for both create and update in service

        // Convert DTO's latitude/longitude to WKT string for the entity's locationGeogWkt field
        if (poiDTO.getLatitude() != null && poiDTO.getLongitude() != null) {
            Point point = geometryFactory.createPoint(new Coordinate(poiDTO.getLongitude(), poiDTO.getLatitude()));
            poi.setLocationGeogWkt(wktWriter.write(point));
        } else {
            // If lat/lon are required for new POIs, this should be validated earlier or throw error
            // For now, if they are null, locationGeogWkt will be null. DB constraint will catch if not nullable.
            log.warn("Latitude/Longitude not provided for POI '{}', locationGeogWkt will be null.", poiDTO.getPoiName());
            poi.setLocationGeogWkt(null); // Explicitly set to null if not provided
        }

        // Handle poiContactsDTO to JSON string
        if (poiDTO.getPoiContacts() != null) {
            poi.setPoiContactsJson(convertContactsToJson(poiDTO.getPoiContacts()));
        }

        // Foreign keys (orgId, createdByUserId) should be directly mapped by MapStruct if DTO has them
        // and entity has corresponding fields. Current Poi entity stores these as UUIDs.
        poi.setOrgId(poiDTO.getOrgId());
        poi.setCreatedByUserId(poiDTO.getCreatedByUserId());
        if (poiDTO.getTownId() != null) {
            poi.setTownId(poiDTO.getTownId());
        }


        return pointOfInterestRepository.save(poi)
            .map(poiMapper::toDTO) // Mapper will convert entity's WKT back to DTO's lat/lon/wkt fields
            .doOnSuccess(dto -> log.info("Saved POI with ID: {}", dto.getPoiId()))
            .doOnError(e -> log.error("Error saving POI: {}", poiDTO.getPoiName(), e));
    }

    public Mono<PointOfInterestDTO> getPoiById(UUID id) {
        log.info("Fetching POI by ID: {}", id);
        return pointOfInterestRepository.findById(id).map(poiMapper::toDTO);
    }

    public Flux<PointOfInterestDTO> getAllPois() {
        log.info("Fetching all POIs");
        return pointOfInterestRepository.findAll().map(poiMapper::toDTO);
    }

    public Flux<PointOfInterestDTO> findPoisByName(String name) {
        log.info("Finding POIs by name containing: {}", name);
        return pointOfInterestRepository.findByPoiNameContainingIgnoreCase(name).map(poiMapper::toDTO);
    }

    public Flux<PointOfInterestDTO> findPoisByType(String type) {
        log.info("Finding POIs by type: {}", type);
        return pointOfInterestRepository.findByPoiType(type).map(poiMapper::toDTO);
    }

    public Flux<PointOfInterestDTO> findPoisByCategory(String category) {
        log.info("Finding POIs by category: {}", category);
        return pointOfInterestRepository.findByPoiCategory(category).map(poiMapper::toDTO);
    }

    @Transactional
    public Mono<PointOfInterestDTO> updatePoi(UUID id, PointOfInterestDTO poiDetailsDTO) {
        log.info("Attempting to update POI with ID: {}", id);
        return pointOfInterestRepository.findById(id)
            .flatMap(poi -> {
                // Update simple fields from DTO using mapper, then override specific ones
                // PointOfInterest updatedPoi = poiMapper.toEntity(poiDetailsDTO); // This maps basic fields - careful with nulls if not handled by mapper

                // Preserve original ID and creation details
                // updatedPoi.setPoiId(poi.getPoiId());
                // updatedPoi.setCreatedAt(poi.getCreatedAt());
                // updatedPoi.setCreatedByUserId(poi.getCreatedByUserId()); // usually not changed
                // updatedPoi.setOrgId(poi.getOrgId()); // Org usually not changed in POI update

                // Fields that can be updated
                poi.setPoiName(poiDetailsDTO.getPoiName() != null ? poiDetailsDTO.getPoiName() : poi.getPoiName());
                poi.setPoiType(poiDetailsDTO.getPoiType() != null ? poiDetailsDTO.getPoiType() : poi.getPoiType());
                poi.setPoiCategory(poiDetailsDTO.getPoiCategory() != null ? poiDetailsDTO.getPoiCategory() : poi.getPoiCategory());
                poi.setPoiLongName(poiDetailsDTO.getPoiLongName());
                poi.setPoiShortName(poiDetailsDTO.getPoiShortName());
                poi.setPoiFriendlyName(poiDetailsDTO.getPoiFriendlyName());
                poi.setPoiDescription(poiDetailsDTO.getPoiDescription());
                poi.setPoiImages(poiDetailsDTO.getPoiImages()); // Assumes full list replacement

                if (poiDetailsDTO.getLatitude() != null && poiDetailsDTO.getLongitude() != null) {
                    Point point = geometryFactory.createPoint(new Coordinate(poiDetailsDTO.getLongitude(), poiDetailsDTO.getLatitude()));
                    poi.setLocationGeogWkt(wktWriter.write(point));
                }
                // If DTO lat/lon are null, existing locationGeogWkt is preserved by not setting it

                if (poiDetailsDTO.getPoiAddress() != null) { // Assuming DTO has AddressTypeDTO
                    poi.setPoiAddressStreetNumber(poiDetailsDTO.getPoiAddress().getStreetNumber());
                    poi.setPoiAddressStreetName(poiDetailsDTO.getPoiAddress().getStreetName());
                    poi.setPoiAddressCity(poiDetailsDTO.getPoiAddress().getCity());
                    poi.setPoiAddressStateProvince(poiDetailsDTO.getPoiAddress().getStateProvince());
                    poi.setPoiAddressPostalCode(poiDetailsDTO.getPoiAddress().getPostalCode());
                    poi.setPoiAddressCountry(poiDetailsDTO.getPoiAddress().getCountry());
                    poi.setPoiAddressInformalAddress(poiDetailsDTO.getPoiAddress().getInformalAddress());
                }

                poi.setPhoneNumber(poiDetailsDTO.getPhoneNumber());
                poi.setWebsiteUrl(poiDetailsDTO.getWebsiteUrl());
                poi.setPoiAmenities(poiDetailsDTO.getPoiAmenities());
                poi.setPoiKeywords(poiDetailsDTO.getPoiKeywords());
                poi.setPoiTypeTags(poiDetailsDTO.getPoiTypeTags());
                poi.setOperationTimePlan(poiDetailsDTO.getOperationTimePlan());

                if (poiDetailsDTO.getPoiContacts() != null) {
                    poi.setPoiContactsJson(convertContactsToJson(poiDetailsDTO.getPoiContacts()));
                } else {
                    poi.setPoiContactsJson(null); // Allow clearing contacts
                }

                if (poiDetailsDTO.getPopularityScore() != null) poi.setPopularityScore(poiDetailsDTO.getPopularityScore());
                if (poiDetailsDTO.getIsActive() != null) poi.setIsActive(poiDetailsDTO.getIsActive());
                poi.setDeactivationReason(poiDetailsDTO.getDeactivationReason());
                if (poiDetailsDTO.getDeactivatedByUserId() != null) poi.setDeactivatedByUserId(poiDetailsDTO.getDeactivatedByUserId());
                if (poiDetailsDTO.getUpdatedByUserId() != null) poi.setUpdatedByUserId(poiDetailsDTO.getUpdatedByUserId());

                poi.setUpdatedAt(OffsetDateTime.now());

                return pointOfInterestRepository.save(poi);
            })
            .map(poiMapper::toDTO)
            .doOnSuccess(dto -> log.info("Successfully updated POI with ID: {}", dto.getPoiId()))
            .doOnError(e -> log.error("Error updating POI ID: {}", id, e));
            // .switchIfEmpty(Mono.error(new RuntimeException("POI not found for update with id " + id))); // TODO: Custom exception
    }

    @Transactional
    public Mono<Void> deletePoi(UUID id) {
        log.info("Deleting POI by ID: {}", id);
        return pointOfInterestRepository.deleteById(id)
            .doOnSuccess(v -> log.info("Deleted POI with ID: {}", id))
            .doOnError(e -> log.error("Error deleting POI ID: {}", id, e));
    }

    public Flux<PointOfInterestDTO> findPoisNearby(double longitude, double latitude, double distance) {
        log.info("Finding POIs nearby to longitude: {}, latitude: {}, within distance: {}", longitude, latitude, distance);
        Point searchPoint = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        String wktSearchPoint = wktWriter.write(searchPoint);

        return pointOfInterestRepository.findNearby(wktSearchPoint, distance)
                                     .map(poiMapper::toDTO);
    }

    public Flux<PointOfInterestDTO> findPoisByTownId(UUID townId) {
        log.info("Finding POIs by townId: {}", townId);
        return pointOfInterestRepository.findByTownId(townId).map(poiMapper::toDTO);
    }

    public Flux<PointOfInterestDTO> findPoisByStateProvince(String stateProvince) {
        log.info("Finding POIs by state/province: {}", stateProvince);
        return pointOfInterestRepository.findByPoiAddressStateProvinceIgnoreCase(stateProvince).map(poiMapper::toDTO);
    }

    public Flux<PointOfInterestDTO> findPoisByCreatorUserId(UUID userId) {
        log.info("Finding POIs by creator user ID: {}", userId);
        return pointOfInterestRepository.findByCreatedByUserId(userId).map(poiMapper::toDTO);
    }

    public Flux<PointOfInterestDTO> findPoisOrderByPopularityScoreDesc() {
        log.info("Finding all POIs ordered by popularity score descending");
        return pointOfInterestRepository.findAllByOrderByPopularityScoreDesc().map(poiMapper::toDTO);
    }

    public Flux<PointOfInterestDTO> findActivePoisOrderByPopularityScoreDesc() {
        log.info("Finding active POIs ordered by popularity score descending");
        return pointOfInterestRepository.findByIsActiveTrueOrderByPopularityScoreDesc().map(poiMapper::toDTO);
    }
}
