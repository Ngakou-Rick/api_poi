package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PodcastEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface PodcastRepository extends ReactiveCrudRepository<PodcastEntity, UUID> {
    Flux<PodcastEntity> findAllByAuthorId(UUID authorId);
}
