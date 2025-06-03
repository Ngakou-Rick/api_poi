package com.poi.yow_point.interfaces;

import com.poi.yow_point.config.GeometryFactoryProvider; // Assurez-vous que le chemin est correct
import com.poi.yow_point.dto.PoiDTO;
import com.poi.yow_point.models.Poi;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Pour une conversion de liste plus concise

@Mapper(componentModel = "spring", uses = { GeometryFactoryProvider.class })
public interface PoiMapper {

    /*
     * // GeometryFactoryProvider pour l'injecter dans les méthodes de mapping de
     * Point
     * // (si vous ne voulez pas le passer en paramètre à chaque fois)
     * 
     * @Mapping(source = "location", target = "latitude", qualifiedByName =
     * "pointToLatitude")
     * 
     * @Mapping(source = "location", target = "longitude", qualifiedByName =
     * "pointToLongitude")
     * 
     * @Mapping(source = "informal_address", target = "informalAddress") // Mappe le
     * champ de l'entité au DTO
     * PoiDTO toDto(Poi poi);
     * 
     * @Mapping(source = "latitude", target = "location", qualifiedByName =
     * "coordinatesToPoint")
     * 
     * @Mapping(source = "longitude", target = "location", qualifiedByName =
     * "coordinatesToPoint") // Réutilise la même
     * // méthode
     * 
     * @Mapping(source = "informalAddress", target = "informal_address") // Mappe le
     * champ du DTO à l'entité
     * Poi toEntity(PoiDTO poiDTO);
     * 
     * List<PoiDTO> toDtoList(List<Poi> pois);
     * 
     * List<Poi> toEntityList(List<PoiDTO> poiDTOs);
     * 
     * // --- Méthodes qualifiées pour le mapping de Point <-> Latitude/Longitude
     * ---
     * // Ces méthodes seront utilisées par MapStruct grâce à qualifiedByName
     * 
     * @Named("pointToLatitude")
     * default Double pointToLatitude(Point point) {
     * return (point != null) ? point.getY() : null;
     * }
     * 
     * @Named("pointToLongitude")
     * default Double pointToLongitude(Point point) {
     * return (point != null) ? point.getX() : null;
     * }
     * 
     * // Pour cette méthode, nous avons besoin d'un GeometryFactory.
     * // Soit on l'injecte dans le mapper, soit on utilise un provider.
     * // Le plus simple avec MapStruct est souvent de passer les deux params et de
     * // laisser
     * // MapStruct comprendre. Mais pour la réutilisation, un Named est bien.
     * // Ici, on suppose que GeometryFactory est disponible (voir
     * // GeometryFactoryProvider plus bas)
     * // ou vous pouvez l'injecter directement dans le Mapper si vous n'utilisez
     * pas
     * // un Provider séparé.
     * 
     * @Named("coordinatesToPoint")
     * default Point coordinatesToPoint(PoiDTO dto, @Autowired GeometryFactory
     * geometryFactory) {
     * if (dto.getLatitude() != null && dto.getLongitude() != null) {
     * return geometryFactory.createPoint(new Coordinate(dto.getLongitude(),
     * dto.getLatitude()));
     * }
     * return null;
     * }
     * 
     * // Si vous ne voulez pas d'@Autowired dans la méthode de mapping,
     * // MapStruct peut aussi injecter GeometryFactory dans le mapper lui-même
     * // et vous l'utilisez dans les méthodes default.
     * 
     * // Initialisation des listes pour éviter les nulls dans l'entité si le DTO
     * les
     * // omet
     * default List<String> mapOptionalList(List<String> list) {
     * return list == null ? new ArrayList<>() : new ArrayList<>(list);
     * }
     * 
     * // Application des initialisations de listes (MapStruct ne le fait pas par
     * // défaut pour les collections)
     * // Ceci est un exemple, dans la pratique, l'initialisation dans le DTO et
     * // l'entité est souvent suffisante.
     * // @AfterMapping
     * // default void initializeCollections(PoiDTO dto, @MappingTarget Poi entity)
     * {
     * // if (entity.getCatalogue() == null) {
     * // entity.setCatalogue(new ArrayList<>());
     * // }
     * // if (entity.getOpeningHours() == null) {
     * // entity.setOpeningHours(new ArrayList<>());
     * // }
     * // }
     * }
     * 
     * // Classe Provider pour GeometryFactory si vous voulez l'injecter proprement
     * via
     * // Spring dans le Mapper
     * 
     * @org.springframework.stereotype.Component
     * class GeometryFactoryProvider {
     * private final GeometryFactory geometryFactory;
     * 
     * public GeometryFactoryProvider() {
     * // SRID 4326 pour WGS84
     * this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
     * }
     * 
     * public GeometryFactory geometryFactory() {
     * return geometryFactory;
     * }
     * 
     */

    @Mapping(source = "location", target = "latitude", qualifiedByName = "pointToLatitude")
    @Mapping(source = "location", target = "longitude", qualifiedByName = "pointToLongitude")
    @Mapping(source = "informal_address", target = "informalAddress")
    @Mapping(target = "catalogue", qualifiedByName = "ensureListIsNotNull")
    @Mapping(target = "openingHours", qualifiedByName = "ensureListIsNotNull")
    PoiDTO toDto(Poi poi);

    @Mapping(target = "location", source = "dto", qualifiedByName = "coordinatesToPoint")
    @Mapping(source = "informalAddress", target = "informal_address")
    @Mapping(target = "catalogue", qualifiedByName = "ensureListIsNotNullFromDto")
    @Mapping(target = "openingHours", qualifiedByName = "ensureListIsNotNullFromDto")
    Poi toEntity(PoiDTO dto, @Context GeometryFactoryProvider factoryProvider);

    // La conversion de liste de DTO vers Entité doit aussi utiliser le contexte.
    // MapStruct peut souvent générer cela si la méthode unitaire est définie.
    // Si MapStruct ne le génère pas automatiquement, vous pouvez le définir comme
    // ceci :
    default List<Poi> toEntityList(List<PoiDTO> dtoList, @Context GeometryFactoryProvider factoryProvider) {
        if (dtoList == null) {
            return null;
        }
        return dtoList.stream()
                .map(dto -> toEntity(dto, factoryProvider))
                .collect(Collectors.toList());
    }

    // MapStruct générera généralement cette méthode si toDto(Poi) est défini.
    List<PoiDTO> toDtoList(List<Poi> pois);

    // --- Méthodes qualifiées ---

    @Named("pointToLatitude")
    default Double pointToLatitude(Point point) {
        return (point != null) ? point.getY() : null;
    }

    @Named("pointToLongitude")
    default Double pointToLongitude(Point point) {
        return (point != null) ? point.getX() : null;
    }

    // Nom de la méthode qualifiée simplifié pour refléter son objectif principal.
    // Elle prend le DTO et le provider pour obtenir le GeometryFactory.
    @Named("coordinatesToPoint")
    default Point convertCoordinatesToPoint(PoiDTO dto, @Context GeometryFactoryProvider factoryProvider) {
        if (dto.getLatitude() != null && dto.getLongitude() != null && factoryProvider != null) {
            GeometryFactory geometryFactory = factoryProvider.provideFactory(); // Utilise la méthode du provider
            if (geometryFactory != null) {
                return geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
            }
        }
        return null;
    }

    @Named("ensureListIsNotNullFromDto")
    default List<String> ensureListIsNotNullFromDto(List<String> list) {
        return list == null ? new ArrayList<>() : list; // Retourne la liste si non nulle, sinon une nouvelle liste
    }

    @Named("ensureListIsNotNull")
    default List<String> ensureListIsNotNull(List<String> list) {
        return list == null ? new ArrayList<>() : list; // Idem pour le mapping vers DTO
    }
}