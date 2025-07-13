package com.poi.yow_point.infrastructure.repositories.blog;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poi.yow_point.infrastructure.entities.Blog;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BlogRepository extends R2dbcRepository<Blog, UUID> {
    @Query("SELECT * FROM blog WHERE user_id = :userId AND is_active = true ORDER BY created_at DESC")
    Flux<Blog> findByUserIdAndIsActiveTrue(@Param("userId") UUID userId);

    @Query("SELECT * FROM blog WHERE poi_id = :poiId AND is_active = true ORDER BY created_at DESC")
    Flux<Blog> findByPoiIdAndIsActiveTrue(@Param("poiId") UUID poiId);

    @Query("SELECT * FROM blog WHERE title ILIKE :title AND is_active = true ORDER BY created_at DESC")
    Flux<Blog> findByTitleContainingIgnoreCaseAndIsActiveTrue(@Param("title") String title);

    @Query("SELECT * FROM blog WHERE is_active = true ORDER BY created_at DESC")
    Flux<Blog> findAllActiveBlogs();

    @Query("SELECT * FROM blog WHERE blog_id = :blogId AND is_active = true")
    Mono<Blog> findByIdAndIsActiveTrue(@Param("blogId") UUID blogId);

    @Query("SELECT COUNT(*) FROM blog WHERE user_id = :userId AND is_active = true")
    Mono<Long> countByUserIdAndIsActiveTrue(@Param("userId") UUID userId);

    @Query("SELECT COUNT(*) FROM blog WHERE poi_id = :poiId AND is_active = true")
    Mono<Long> countByPoiIdAndIsActiveTrue(@Param("poiId") UUID poiId);
}