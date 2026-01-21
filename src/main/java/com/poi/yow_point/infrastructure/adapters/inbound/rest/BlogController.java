package com.poi.yow_point.infrastructure.adapters.inbound.rest;

import com.poi.yow_point.domain.ports.in.BlogPort;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.BlogDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog API", description = "Endpoints for managing blog posts")
public class BlogController {

    private final BlogPort service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new blog post")
    public Mono<BlogDTO> createBlog(@Valid @RequestBody BlogDTO blogDTO) {
        return service.createBlog(blogDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a blog post by ID")
    public Mono<BlogDTO> getBlogById(@PathVariable UUID id) {
        return service.getBlogById(id);
    }

    @GetMapping
    @Operation(summary = "Get all blog posts")
    public Flux<BlogDTO> getAllBlogs() {
        return service.getAllBlogs();
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get blog posts by author ID")
    public Flux<BlogDTO> getBlogsByAuthorId(@PathVariable UUID authorId) {
        return service.getBlogsByAuthorId(authorId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a blog post")
    public Mono<BlogDTO> updateBlog(@PathVariable UUID id, @Valid @RequestBody BlogDTO blogDTO) {
        return service.updateBlog(id, blogDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a blog post")
    public Mono<Void> deleteBlog(@PathVariable UUID id) {
        return service.deleteBlog(id);
    }
}
