package com.poi.yow_point.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poi.yow_point.models.PointOfInterest;
import com.poi.yow_point.services.PointOfInterestService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pois") // Base path for POI-related APIs
public class PointOfInterestController {

    private final PointOfInterestService pointOfInterestService;

    @Autowired
    public PointOfInterestController(PointOfInterestService pointOfInterestService) {
        this.pointOfInterestService = pointOfInterestService;
    }

    @PostMapping
    public ResponseEntity<PointOfInterest> createPoi(@RequestBody PointOfInterest poi) {
        PointOfInterest savedPoi = pointOfInterestService.savePoi(poi);
        return new ResponseEntity<>(savedPoi, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PointOfInterest> getPoiById(@PathVariable UUID id) {
        return pointOfInterestService.getPoiById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PointOfInterest>> getAllPois(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {
        
        List<PointOfInterest> pois;
        if (name != null) {
            pois = pointOfInterestService.findPoisByName(name);
        } else if (type != null) {
            pois = pointOfInterestService.findPoisByType(type);
        } else if (category != null) {
            pois = pointOfInterestService.findPoisByCategory(category);
        } else {
            pois = pointOfInterestService.getAllPois();
        }
        return ResponseEntity.ok(pois);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PointOfInterest> updatePoi(@PathVariable UUID id, @RequestBody PointOfInterest poiDetails) {
        try {
            PointOfInterest updatedPoi = pointOfInterestService.updatePoi(id, poiDetails);
            return ResponseEntity.ok(updatedPoi);
        } catch (RuntimeException e) { // Replace with specific exception like PoiNotFoundException
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoi(@PathVariable UUID id) {
        pointOfInterestService.deletePoi(id);
        return ResponseEntity.noContent().build();
    }
    
    // TODO: Add endpoints for reviews, access logs, stats, and spatial queries
}

