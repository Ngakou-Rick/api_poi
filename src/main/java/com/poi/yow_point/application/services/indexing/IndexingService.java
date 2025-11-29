package com.poi.yow_point.application.services.indexing;

import com.poi.yow_point.infrastructure.document.PoiDocument;
import com.poi.yow_point.infrastructure.entities.PointOfInterest;
import com.poi.yow_point.infrastructure.repositories.poiDocument.PoiDocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class IndexingService {

    private final PoiDocumentRepository poiDocumentRepository;

    public IndexingService(PoiDocumentRepository poiDocumentRepository) {
        this.poiDocumentRepository = poiDocumentRepository;
    }

    public Mono<Void> indexPoi(PointOfInterest poi) {
        PoiDocument poiDocument = toPoiDocument(poi);
        return poiDocumentRepository.save(poiDocument)
                .doOnSuccess(doc -> log.info("Successfully indexed POI with ID: {}", doc.getPoiId()))
                .doOnError(e -> log.error("Failed to index POI with ID: {}", poi.getPoiId(), e))
                .then();
    }

    public Mono<Void> deletePoi(PointOfInterest poi) {
        return poiDocumentRepository.deleteById(poi.getPoiId().toString())
                .doOnSuccess(doc -> log.info("Successfully deleted POI with ID: {}", poi.getPoiId()))
                .doOnError(e -> log.error("Failed to delete POI with ID: {}", poi.getPoiId(), e));
    }

    private PoiDocument toPoiDocument(PointOfInterest poi) {
        return PoiDocument.builder()
                .id(poi.getPoiId().toString())
                .poiId(poi.getPoiId())
                .createdByUserId(poi.getCreatedByUserId())
                .organizationId(poi.getOrganizationId())
                .poiName(poi.getPoiName())
                .poiType(poi.getPoiType())
                .poiCategory(poi.getPoiCategory())
                .poiDescription(poi.getPoiDescription())
                .location(new GeoPoint(poi.getLocation().getY(), poi.getLocation().getX()))
                .addressStreetNumber(poi.getAddressStreetNumber())
                .addressStreetName(poi.getAddressStreetName())
                .addressCity(poi.getAddressCity())
                .addressPostalCode(poi.getAddressPostalCode())
                .addressCountry(poi.getAddressCountry())
                .stateProvince(poi.getStateProvince())
                .informalAddress(poi.getInformalAddress())
                .websiteUrl(poi.getWebsiteUrl())
                .operationTimePlan(poi.getOperationTimePlan())
                .poiContacts(poi.getPoiContacts())
                .poiImagesUrls(poi.getPoiImagesUrlsList())
                .poiAmenities(poi.getPoiAmenitiesList())
                .poiKeywords(poi.getPoiKeywordsList())
                .popularityScore(poi.getPopularityScore())
                .isActive(poi.getIsActive())
                .createdAt(poi.getCreatedAt())
                .updatedAt(poi.getUpdatedAt())
                .build();
    }
}
