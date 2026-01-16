package com.poi.yow_point.domain.ports.out;

import com.poi.yow_point.domain.model.PoiReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface PoiReviewRepositoryPort {
    Mono<PoiReview> save(PoiReview review);
    Mono<PoiReview> findById(UUID id);
    Flux<PoiReview> findAll();
    Mono<Void> deleteById(UUID id);
}
