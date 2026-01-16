package com.poi.yow_point.domain.ports.out;

import com.poi.yow_point.domain.model.PointOfInterest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface PointOfInterestRepositoryPort {
    Mono<PointOfInterest> save(PointOfInterest poi);
    Mono<PointOfInterest> findById(UUID id);
    Flux<PointOfInterest> findAll();
    Mono<Void> deleteById(UUID id);
    Mono<Boolean> existsById(UUID id);
}
