package com.poi.yow_point.application.service;

import com.poi.yow_point.domain.ports.in.BlogPort;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.BlogDTO;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository.BlogRepository;
import com.poi.yow_point.infrastructure.mappers.BlogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogPort {

    private final BlogRepository repository;
    private final BlogMapper mapper;

    @Override
    public Mono<BlogDTO> createBlog(BlogDTO blogDTO) {
        var entity = mapper.toEntity(mapper.toDomain(blogDTO));
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity).map(mapper::toDto);
    }

    @Override
    public Mono<BlogDTO> getBlogById(UUID blogId) {
        return repository.findById(blogId).map(mapper::toDto);
    }

    @Override
    public Flux<BlogDTO> getAllBlogs() {
        return repository.findAll().map(mapper::toDto);
    }

    @Override
    public Flux<BlogDTO> getBlogsByAuthorId(UUID authorId) {
        return repository.findAllByAuthorId(authorId).map(mapper::toDto);
    }

    @Override
    public Mono<BlogDTO> updateBlog(UUID blogId, BlogDTO blogDTO) {
        return repository.findById(blogId)
                .flatMap(existing -> {
                    existing.setTitle(blogDTO.getTitle());
                    existing.setContent(blogDTO.getContent());
                    existing.setImageUrl(blogDTO.getImageUrl());
                    existing.setTags(blogDTO.getTags());
                    existing.setUpdatedAt(OffsetDateTime.now());
                    return repository.save(existing);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> deleteBlog(UUID blogId) {
        return repository.deleteById(blogId);
    }
}
