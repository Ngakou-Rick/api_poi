package com.poi.yow_point.domain.ports.in;

import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.BlogDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BlogPort {
    Mono<BlogDTO> createBlog(BlogDTO blogDTO);
    Mono<BlogDTO> getBlogById(UUID blogId);
    Flux<BlogDTO> getAllBlogs();
    Flux<BlogDTO> getBlogsByAuthorId(UUID authorId);
    Mono<BlogDTO> updateBlog(UUID blogId, BlogDTO blogDTO);
    Mono<Void> deleteBlog(UUID blogId);
}
