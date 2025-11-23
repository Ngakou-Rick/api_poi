package com.poi.yow_point.infrastructure.elasticsearch.repository;

import com.poi.yow_point.infrastructure.elasticsearch.document.PoiDocument;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface PoiDocumentRepository extends ReactiveElasticsearchRepository<PoiDocument, String> {
    Flux<PoiDocument> findByLocationNear(GeoPoint location, String distance);
    Flux<PoiDocument> findTopByOrderByPopularityScoreDesc(int limit);
    Flux<PoiDocument> findByPoiNameContainingIgnoreCase(String name);
    Flux<PoiDocument> findByPoiType(String poiType);
    Flux<PoiDocument> findByPoiCategory(String poiCategory);
    Flux<PoiDocument> findByAddressCity(String city);
}
