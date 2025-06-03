package com.poi.yow_point.interfaces;

import com.poi.yow_point.dto.PoiDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PoiInterface {

    PoiDTO createPoi(PoiDTO poiDTO);

    Optional<PoiDTO> getPoiById(UUID id);

    List<PoiDTO> getAllPois();

    Optional<PoiDTO> updatePoi(UUID id, PoiDTO poiDTO);

    boolean deletePoi(UUID id);

    List<PoiDTO> findPoisByCategory(String category);

    List<PoiDTO> findPoisByNameContaining(String nameFragment);

    List<PoiDTO> findPoisByCountry(String country);

    List<PoiDTO> findPoisByCity(String city);

    List<PoiDTO> findPoisByCountryAndCity(String country, String city);

    List<PoiDTO> findNearbyPois(double latitude, double longitude, double distanceMeters);
}
