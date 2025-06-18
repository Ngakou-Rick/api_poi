package com.poi.yow_point.services;

import com.poi.yow_point.dto.PointOfInterestDTO;
import com.poi.yow_point.mappers.PointOfInterestMapper;
import com.poi.yow_point.models.AppUser;
import com.poi.yow_point.models.Organization;
import com.poi.yow_point.models.PointOfInterest;
import com.poi.yow_point.repositories.AppUserRepository;
import com.poi.yow_point.repositories.OrganizationRepository;
import com.poi.yow_point.repositories.PointOfInterestRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final OrganizationRepository organizationRepository;
    private final AppUserRepository appUserRepository;

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

    @Transactional
    public PointOfInterestDTO savePoi(PointOfInterestDTO poiDTO) {
        log.info("Saving new POI: {}", poiDTO.getPoiName());
        PointOfInterest poi = poiMapper.toEntity(poiDTO);

        Organization organization = organizationRepository.findById(poiDTO.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found for ID: " + poiDTO.getOrgId()));
        poi.setOrganization(organization);

        AppUser createdBy = appUserRepository.findById(poiDTO.getCreatedByUserId())
                .orElseThrow(() -> new RuntimeException("Creator user not found for ID: " + poiDTO.getCreatedByUserId()));
        poi.setCreatedBy(createdBy);

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
        return pointOfInterestRepository.findByPoiTypeIgnoreCase(type).stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    public List<PointOfInterestDTO> findPoisByCategory(String category) {
        log.info("Finding POIs by category: {}", category);
        return pointOfInterestRepository.findByPoiCategoryIgnoreCase(category).stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public PointOfInterestDTO updatePoi(UUID id, PointOfInterestDTO poiDetailsDTO) {
        log.info("Updating POI with ID: {}", id);
        PointOfInterest poi = pointOfInterestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("POI not found for ID: " + id));

        // Use the mapper to update the entity from the DTO
        poiMapper.updatePoiFromDto(poiDetailsDTO, poi);

        // Manually handle relationships that require database lookups
        if (poiDetailsDTO.getOrgId() != null && !poiDetailsDTO.getOrgId().equals(poi.getOrganization().getOrganizationId())) {
            Organization organization = organizationRepository.findById(poiDetailsDTO.getOrgId())
                    .orElseThrow(() -> new RuntimeException("Organization not found for ID: " + poiDetailsDTO.getOrgId()));
            poi.setOrganization(organization);
        }

        if (poiDetailsDTO.getUpdatedByUserId() != null) {
            AppUser updatedBy = appUserRepository.findById(poiDetailsDTO.getUpdatedByUserId())
                    .orElseThrow(() -> new RuntimeException("Updating user not found for ID: " + poiDetailsDTO.getUpdatedByUserId()));
            poi.setUpdatedBy(updatedBy);
        }

        poi.setUpdatedAt(OffsetDateTime.now()); // Always update timestamp

        PointOfInterest updatedPoi = pointOfInterestRepository.save(poi);
        log.info("Successfully updated POI with ID: {}", updatedPoi.getPoiId());
        return poiMapper.toDTO(updatedPoi);
    }

    @Transactional
    public void deletePoi(UUID id) {
        log.info("Deleting POI by ID: {}", id);
        pointOfInterestRepository.deleteById(id);
        log.info("Deleted POI with ID: {}", id);
    }

    @Transactional
    public List<PointOfInterestDTO> findPoisNearby(double longitude, double latitude, double distance) {
        log.info("Finding POIs nearby to longitude: {}, latitude: {}, within distance: {}", longitude, latitude, distance);
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point searchPoint = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        return pointOfInterestRepository.findNearby(searchPoint, distance)
                .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<PointOfInterestDTO> findPoisByTownId(UUID townId) {
        log.info("Finding POIs by townId: {}", townId);
        return pointOfInterestRepository.findByTownId(townId)
                .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<PointOfInterestDTO> findPoisByStateProvince(String stateProvince) {
        log.info("Finding POIs by state/province: {}", stateProvince);
        return pointOfInterestRepository.findByPoiAddressStateProvinceIgnoreCase(stateProvince)
                .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<PointOfInterestDTO> findPoisByCreatorUserId(UUID userId) {
        log.info("Finding POIs by creator user ID: {}", userId);
        return pointOfInterestRepository.findByCreatedByUserId(userId)
                .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<PointOfInterestDTO> findPoisOrderByPopularityScoreDesc() {
        log.info("Finding all POIs ordered by popularity score descending");
        return pointOfInterestRepository.findAllByOrderByPopularityScoreDesc()
                .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<PointOfInterestDTO> findActivePoisOrderByPopularityScoreDesc() {
        log.info("Finding active POIs ordered by popularity score descending");
        return pointOfInterestRepository.findByIsActiveTrueOrderByPopularityScoreDesc()
                .stream().map(poiMapper::toDTO).collect(Collectors.toList());
    }
}