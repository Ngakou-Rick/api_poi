package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.BlogEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface BlogRepository extends ReactiveCrudRepository<BlogEntity, UUID> {
    Flux<BlogEntity> findAllByAuthorId(UUID authorId);
}
