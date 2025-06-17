package com.poi.yow_point.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poi.yow_point.models.PointOfInterest;
import com.poi.yow_point.repositories.PointOfInterestRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PointOfInterestService {

    private final PointOfInterestRepository pointOfInterestRepository;

    @Autowired
    public PointOfInterestService(PointOfInterestRepository pointOfInterestRepository) {
        this.pointOfInterestRepository = pointOfInterestRepository;
    }

    public PointOfInterest savePoi(PointOfInterest poi) {
        // TODO: Add any validation or business logic before saving
        return pointOfInterestRepository.save(poi);
    }

    public Optional<PointOfInterest> getPoiById(UUID id) {
        return pointOfInterestRepository.findById(id);
    }

    public List<PointOfInterest> getAllPois() {
        return pointOfInterestRepository.findAll();
    }
    
    public List<PointOfInterest> findPoisByName(String name) {
        return pointOfInterestRepository.findByPoiNameContainingIgnoreCase(name);
    }

    public List<PointOfInterest> findPoisByType(String type) {
        return pointOfInterestRepository.findByPoiType(type);
    }

    public List<PointOfInterest> findPoisByCategory(String category) {
        return pointOfInterestRepository.findByPoiCategory(category);
    }

    public PointOfInterest updatePoi(UUID id, PointOfInterest poiDetails) {
        PointOfInterest poi = pointOfInterestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("POI not found with id " + id)); // TODO: Use custom exception

        // Update fields - example for name, add others as needed
        poi.setPoiName(poiDetails.getPoiName());
        poi.setPoiType(poiDetails.getPoiType());
        poi.setPoiCategory(poiDetails.getPoiCategory());
        poi.setPoiLongName(poiDetails.getPoiLongName());
        poi.setPoiShortName(poiDetails.getPoiShortName());
        poi.setPoiFriendlyName(poiDetails.getPoiFriendlyName());
        poi.setPoiDescription(poiDetails.getPoiDescription());
        // ... update other modifiable fields ...
        poi.setPoiAddress(poiDetails.getPoiAddress());
        poi.setPhoneNumber(poiDetails.getPhoneNumber());
        poi.setWebsiteUrl(poiDetails.getWebsiteUrl());
        poi.setPoiAmenities(poiDetails.getPoiAmenities());
        poi.setPoiKeywords(poiDetails.getPoiKeywords());
        poi.setPoiTypeTags(poiDetails.getPoiTypeTags());
        poi.setOperationTimePlan(poiDetails.getOperationTimePlan());
        poi.setPoiContacts(poiDetails.getPoiContacts());
        // isActive, deactivationReason, deactivatedBy would likely be handled by specific methods
        // popularityScore might be calculated
        // orgId, createdBy, createdAt are usually not updated directly

        return pointOfInterestRepository.save(poi);
    }


    public void deletePoi(UUID id) {
        pointOfInterestRepository.deleteById(id);
    }

    // TODO: Add methods for spatial searches, review management, etc.
}
