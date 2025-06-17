package com.yowyob.yowyob_point_of_interest_api.service;

import com.yowyob.yowyob_point_of_interest_api.dto.PointOfInterestDTO;
import com.yowyob.yowyob_point_of_interest_api.mapper.PointOfInterestMapper;
import com.yowyob.yowyob_point_of_interest_api.model.AppUser;
import com.yowyob.yowyob_point_of_interest_api.model.Organization;
import com.yowyob.yowyob_point_of_interest_api.model.PointOfInterest;
import com.yowyob.yowyob_point_of_interest_api.repository.AppUserRepository;
import com.yowyob.yowyob_point_of_interest_api.repository.OrganizationRepository;
import com.yowyob.yowyob_point_of_interest_api.repository.PointOfInterestRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PointOfInterestService {

    private static final Logger log = LoggerFactory.getLogger(PointOfInterestService.class);
    private final PointOfInterestRepository pointOfInterestRepository;
    private final PointOfInterestMapper poiMapper;
    private final OrganizationRepository organizationRepository; // For setting Organization on POI
    private final AppUserRepository appUserRepository;       // For setting AppUser (creator/updater) on POI


    @Autowired
    public PointOfInterestService(PointOfInterestRepository pointOfInterestRepository,
                                  PointOfInterestMapper poiMapper,
                                  OrganizationRepository organizationRepository,
                                  AppUserRepository appUserRepository) {
        this.pointOfInterestRepository = pointOfInterestRepository;
        this.poiMapper = poiMapper;
        this.organizationRepository = organizationRepository;
        this.appUserRepository = appUserRepository;
    }

    public PointOfInterestDTO savePoi(PointOfInterestDTO poiDTO) {
        log.info("Saving POI: {}", poiDTO.getPoiName());
        PointOfInterest poi = poiMapper.toEntity(poiDTO);

        Organization organization = organizationRepository.findById(poiDTO.getOrgId())
            .orElseThrow(() -> new RuntimeException("Organization not found for ID: " + poiDTO.getOrgId()));
        poi.setOrganization(organization);

        AppUser createdBy = appUserRepository.findById(poiDTO.getCreatedByUserId())
            .orElseThrow(() -> new RuntimeException("Creator user not found for ID: " + poiDTO.getCreatedByUserId()));
        poi.setCreatedBy(createdBy);

        // Handle Point geometry from DTO (assuming DTO has lat/lon or WKT string for locationGeog)
        // For now, locationGeog in DTO is String, so mapper will pass it.
        // If DTO had lat/lon:
        // GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        // Point point = geometryFactory.createPoint(new Coordinate(poiDTO.getLongitude(), poiDTO.getLatitude()));
        // poi.setLocationGeog(point);


        PointOfInterest savedPoi = pointOfInterestRepository.save(poi);
        log.info("Saved POI with ID: {}", savedPoi.getPoiId());
        return poiMapper.toDTO(savedPoi);
    }

    public Optional<PointOfInterestDTO> getPoiById(UUID id) {
        log.info("Fetching POI by ID: {}", id);
        return pointOfInterestRepository.findById(id).map(poiMapper::toDTO);
    }

    public List<PointOfInterestDTO> getAllPois() {
        log.info("Fetching all POIs");
        return pointOfInterestRepository.findAll().stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public List<PointOfInterestDTO> findPoisByName(String name) {
        log.info("Finding POIs by name containing: {}", name);
        return pointOfInterestRepository.findByPoiNameContainingIgnoreCase(name).stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public List<PointOfInterestDTO> findPoisByType(String type) {
        log.info("Finding POIs by type: {}", type);
        return pointOfInterestRepository.findByPoiType(type).stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public List<PointOfInterestDTO> findPoisByCategory(String category) {
        log.info("Finding POIs by category: {}", category);
        return pointOfInterestRepository.findByPoiCategory(category).stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public PointOfInterestDTO updatePoi(UUID id, PointOfInterestDTO poiDetailsDTO) {
        log.info("Attempting to update POI with ID: {}", id);
        PointOfInterest poi = pointOfInterestRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("POI not found with ID: {}", id);
                    return new RuntimeException("POI not found with id " + id);
                });

        log.debug("Updating fields for POI ID: {}", id);
        // Update simple fields from DTO
        poi.setPoiName(poiDetailsDTO.getPoiName());
        poi.setPoiType(poiDetailsDTO.getPoiType());
        poi.setPoiCategory(poiDetailsDTO.getPoiCategory());
        poi.setPoiLongName(poiDetailsDTO.getPoiLongName());
        poi.setPoiShortName(poiDetailsDTO.getPoiShortName());
        poi.setPoiFriendlyName(poiDetailsDTO.getPoiFriendlyName());
        poi.setPoiDescription(poiDetailsDTO.getPoiDescription());
        // poi.setPoiLogo(poiDetailsDTO.getPoiLogo()); // if handled as byte[] in DTO
        poi.setPoiImages(poiDetailsDTO.getPoiImages());

        // poi.setLocationGeog(point); // If DTO provides data to reconstruct Point

        if (poiDetailsDTO.getPoiAddress() != null) {
            // Assuming AddressTypeMapper is accessible via PointOfInterestMapper instance or direct injection
            poi.setPoiAddress(PointOfInterestMapper.INSTANCE.getAddressTypeMapper().toEntity(poiDetailsDTO.getPoiAddress()));
        } else {
            poi.setPoiAddress(null);
        }
        poi.setPhoneNumber(poiDetailsDTO.getPhoneNumber());
        poi.setWebsiteUrl(poiDetailsDTO.getWebsiteUrl());
        poi.setPoiAmenities(poiDetailsDTO.getPoiAmenities());
        poi.setPoiKeywords(poiDetailsDTO.getPoiKeywords());
        poi.setPoiTypeTags(poiDetailsDTO.getPoiTypeTags());
        poi.setOperationTimePlan(poiDetailsDTO.getOperationTimePlan()); // Assuming String
        if (poiDetailsDTO.getPoiContacts() != null) {
             poi.setPoiContacts(poiDetailsDTO.getPoiContacts().stream()
                                       .map(PointOfInterestMapper.INSTANCE.getContactPersonTypeMapper()::toEntity)
                                       .collect(Collectors.toList()));
        } else {
            poi.setPoiContacts(null);
        }
        poi.setPopularityScore(poiDetailsDTO.getPopularityScore());
        poi.setActive(poiDetailsDTO.isActive());
        poi.setDeactivationReason(poiDetailsDTO.getDeactivationReason());

        if (poiDetailsDTO.getOrgId() != null && (poi.getOrganization() == null || !poi.getOrganization().getOrganizationId().equals(poiDetailsDTO.getOrgId()))) {
             Organization organization = organizationRepository.findById(poiDetailsDTO.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found for ID: " + poiDetailsDTO.getOrgId()));
            poi.setOrganization(organization);
        }
        if (poiDetailsDTO.getDeactivatedByUserId() != null) {
            if (poi.getDeactivatedBy() == null || !poi.getDeactivatedBy().getUserId().equals(poiDetailsDTO.getDeactivatedByUserId())) {
                 AppUser deactivatedBy = appUserRepository.findById(poiDetailsDTO.getDeactivatedByUserId())
                    .orElseThrow(() -> new RuntimeException("Deactivating user not found for ID: " + poiDetailsDTO.getDeactivatedByUserId()));
                poi.setDeactivatedBy(deactivatedBy);
            }
        } else {
            poi.setDeactivatedBy(null);
        }

        if (poiDetailsDTO.getUpdatedByUserId() != null) {
            if(poi.getUpdatedBy() == null || !poi.getUpdatedBy().getUserId().equals(poiDetailsDTO.getUpdatedByUserId())) {
                AppUser updatedBy = appUserRepository.findById(poiDetailsDTO.getUpdatedByUserId())
                    .orElseThrow(() -> new RuntimeException("Updating user not found for ID: " + poiDetailsDTO.getUpdatedByUserId()));
                poi.setUpdatedBy(updatedBy);
            }
        } else {
             poi.setUpdatedBy(null); // Or set to the current user performing the update
        }
        poi.setUpdatedAt(OffsetDateTime.now()); // Always update timestamp

        PointOfInterest updatedPoi = pointOfInterestRepository.save(poi);
        log.info("Successfully updated POI with ID: {}", updatedPoi.getPoiId());
        return poiMapper.toDTO(updatedPoi);
    }

    public void deletePoi(UUID id) {
        log.info("Deleting POI by ID: {}", id);
        pointOfInterestRepository.deleteById(id);
        log.info("Deleted POI with ID: {}", id);
    }

    public List<PointOfInterestDTO> findPoisNearby(double longitude, double latitude, double distance) {
        log.info("Finding POIs nearby to longitude: {}, latitude: {}, within distance: {}", longitude, latitude, distance);
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point searchPoint = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        return pointOfInterestRepository.findNearby(searchPoint, distance)
                                     .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public List<PointOfInterestDTO> findPoisByTownId(UUID townId) {
        log.info("Finding POIs by townId: {}", townId);
        return pointOfInterestRepository.findByTownId(townId)
                                     .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public List<PointOfInterestDTO> findPoisByStateProvince(String stateProvince) {
        log.info("Finding POIs by state/province: {}", stateProvince);
        return pointOfInterestRepository.findByPoiAddressStateProvinceIgnoreCase(stateProvince)
                                     .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public List<PointOfInterestDTO> findPoisByCreatorUserId(UUID userId) {
        log.info("Finding POIs by creator user ID: {}", userId);
        return pointOfInterestRepository.findByCreatedByUserId(userId)
                                     .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public List<PointOfInterestDTO> findPoisOrderByPopularityScoreDesc() {
        log.info("Finding all POIs ordered by popularity score descending");
        return pointOfInterestRepository.findAllByOrderByPopularityScoreDesc()
                                     .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public List<PointOfInterestDTO> findActivePoisOrderByPopularityScoreDesc() {
        log.info("Finding active POIs ordered by popularity score descending");
        return pointOfInterestRepository.findByIsActiveTrueOrderByPopularityScoreDesc()
                                     .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }
}
