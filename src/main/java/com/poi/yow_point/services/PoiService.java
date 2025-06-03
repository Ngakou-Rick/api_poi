package com.poi.yow_point.services;

import com.poi.yow_point.config.GeometryFactoryProvider; // Import
import com.poi.yow_point.dto.PoiDTO;
import com.poi.yow_point.interfaces.PoiInterface;
import com.poi.yow_point.interfaces.PoiMapper;
import com.poi.yow_point.models.Poi;
import com.poi.yow_point.repositories.PoiRepository;
// Retire les imports JTS directs s'ils ne sont plus utilisés ici
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
// Pour la mise à jour manuelle de la location si le mapper ne le fait pas via une méthode update
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Service
public class PoiService implements PoiInterface {

    private static final Logger logger = LoggerFactory.getLogger(PoiService.class);

    private final PoiRepository poiRepository;
    private final PoiMapper poiMapper;
    private final GeometryFactoryProvider geometryFactoryProvider; // Doit être injecté

    @Autowired
    public PoiService(PoiRepository poiRepository, PoiMapper poiMapper,
            GeometryFactoryProvider geometryFactoryProvider) {
        this.poiRepository = poiRepository;
        this.poiMapper = poiMapper;
        this.geometryFactoryProvider = geometryFactoryProvider; // Injection
    }

    @Override
    @Transactional
    public PoiDTO createPoi(PoiDTO poiDTO) {
        logger.info("Creating new PoI: {}", poiDTO.getName());
        // Appel de la méthode toEntity qui nécessite GeometryFactoryProvider
        Poi poi = poiMapper.toEntity(poiDTO, geometryFactoryProvider);
        Poi savedPoi = poiRepository.save(poi);
        return poiMapper.toDto(savedPoi);
    }

    // ... getPoiById et getAllPois restent inchangés car ils utilisent toDto et
    // toDtoList (sans contexte nécessaire)

    @Override
    @Transactional
    public Optional<PoiDTO> updatePoi(UUID id, PoiDTO poiDTO) {
        logger.info("Updating PoI with id: {}", id);
        return poiRepository.findById(id)
                .map(existingPoi -> {
                    // Mettre à jour les champs non-géométriques
                    existingPoi.setName(poiDTO.getName());
                    existingPoi.setCountry(poiDTO.getCountry());
                    existingPoi.setCity(poiDTO.getCity());
                    existingPoi.setDescription(poiDTO.getDescription());
                    existingPoi.setCategory(poiDTO.getCategory());
                    existingPoi.setAddress(poiDTO.getAddress());
                    existingPoi.setInformal_address(poiDTO.getInformalAddress());
                    existingPoi.setCatalogue(poiMapper.ensureListIsNotNullFromDto(poiDTO.getCatalogue()));
                    existingPoi.setOpeningHours(poiMapper.ensureListIsNotNullFromDto(poiDTO.getOpeningHours()));
                    existingPoi.setContactInfo(poiDTO.getContactInfo());
                    existingPoi.setWebsite(poiDTO.getWebsite());

                    // Mettre à jour la localisation
                    if (poiDTO.getLatitude() != null && poiDTO.getLongitude() != null) {
                        // Utiliser directement le geometryFactory obtenu du provider
                        GeometryFactory factory = geometryFactoryProvider.provideFactory();
                        Point newLocation = factory.createPoint(
                                new Coordinate(poiDTO.getLongitude(), poiDTO.getLatitude()));
                        existingPoi.setLocation(newLocation);
                    } else {
                        existingPoi.setLocation(null); // Ou une autre logique si la localisation ne peut être nulle
                    }

                    Poi updatedPoi = poiRepository.save(existingPoi);
                    return poiMapper.toDto(updatedPoi);
                });
    }

    // ... les autres méthodes de recherche restent inchangées car elles utilisent
    // toDtoList ...
    @Override
    @Transactional(readOnly = true)
    public Optional<PoiDTO> getPoiById(UUID id) {
        logger.debug("Fetching PoI with id: {}", id);
        return poiRepository.findById(id).map(poiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoiDTO> getAllPois() {
        logger.debug("Fetching all PoIs");
        return poiMapper.toDtoList(poiRepository.findAll());
    }

    @Override
    @Transactional
    public boolean deletePoi(UUID id) {
        logger.info("Deleting PoI with id: {}", id);
        if (poiRepository.existsById(id)) {
            poiRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoiDTO> findPoisByCategory(String category) {
        logger.debug("Finding PoIs by category: {}", category);
        return poiMapper.toDtoList(poiRepository.findByCategoryIgnoreCase(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoiDTO> findPoisByNameContaining(String nameFragment) {
        logger.debug("Finding PoIs by name containing: {}", nameFragment);
        return poiMapper.toDtoList(poiRepository.findByNameContainingIgnoreCase(nameFragment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoiDTO> findPoisByCountry(String country) {
        logger.debug("Finding PoIs by country: {}", country);
        return poiMapper.toDtoList(poiRepository.findByCountryIgnoreCase(country));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoiDTO> findPoisByCity(String city) {
        logger.debug("Finding PoIs by city: {}", city);
        return poiMapper.toDtoList(poiRepository.findByCityIgnoreCase(city));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoiDTO> findPoisByCountryAndCity(String country, String city) {
        logger.debug("Finding PoIs by country: {} and city: {}", country, city);
        return poiMapper.toDtoList(poiRepository.findByCountryIgnoreCaseAndCityIgnoreCase(country, city));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoiDTO> findNearbyPois(double latitude, double longitude, double distanceMeters) {
        logger.debug("Finding nearby PoIs for lat: {}, lon: {}, distance: {}m", latitude, longitude, distanceMeters);
        return poiMapper.toDtoList(poiRepository.findNearby(latitude, longitude, distanceMeters));
    }
}