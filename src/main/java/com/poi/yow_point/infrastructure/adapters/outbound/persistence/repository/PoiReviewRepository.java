package com.poi.yow_point.infrastructure.adapters.outbound.persistence.repositoryReview;

import com.poi.yow_point.infrastructure.adapters.outbound.persistence.entity.PoiReviewEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
//import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface PoiReviewRepository extends R2dbcRepository<PoiReviewEntity, UUID>, PoiReviewRepositoryCustom {
    // This interface extends R2dbcRepository for basic CRUD operations
    // and PoiReviewRepositoryCustom for custom query methods.

}