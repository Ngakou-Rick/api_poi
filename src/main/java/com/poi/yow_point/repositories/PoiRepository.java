package com.poi.yow_point.repositories;

import com.poi.yow_point.models.Poi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PoiRepository extends JpaRepository<Poi, UUID> {

    List<Poi> findByCategoryIgnoreCase(String category);

    List<Poi> findByNameContainingIgnoreCase(String nameFragment);

    List<Poi> findByCountryIgnoreCase(String country);

    List<Poi> findByCityIgnoreCase(String city);

    List<Poi> findByCountryIgnoreCaseAndCityIgnoreCase(String country, String city);

    @Query(value = "SELECT * FROM points_of_interest p WHERE ST_DWithin(p.location::geography, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)::geography, :distanceMeters)", nativeQuery = true)
    List<Poi> findNearby(
            @Param("lat") double latitude,
            @Param("lon") double longitude,
            @Param("distanceMeters") double distanceMeters);

    // La méthode findByPlaceName n'est plus applicable directement avec
    // country/city.
    // Si vous voulez chercher par ville OU pays, vous pouvez faire :
    // @Query("SELECT p FROM Poi p WHERE LOWER(p.city) = LOWER(:place) OR
    // LOWER(p.country) = LOWER(:place)")
    // List<Poi> findByCityOrCountry(@Param("place") String place);
}
