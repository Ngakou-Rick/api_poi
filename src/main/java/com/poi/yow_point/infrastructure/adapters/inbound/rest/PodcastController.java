package com.poi.yow_point.infrastructure.adapters.inbound.rest;

import com.poi.yow_point.domain.ports.in.PodcastPort;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.PodcastDTO;
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
@RequestMapping("/api/v1/podcasts")
@RequiredArgsConstructor
@Tag(name = "Podcast API", description = "Endpoints for managing podcasts")
public class PodcastController {

    private final PodcastPort service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new podcast")
    public Mono<PodcastDTO> createPodcast(@Valid @RequestBody PodcastDTO podcastDTO) {
        return service.createPodcast(podcastDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a podcast by ID")
    public Mono<PodcastDTO> getPodcastById(@PathVariable UUID id) {
        return service.getPodcastById(id);
    }

    @GetMapping
    @Operation(summary = "Get all podcasts")
    public Flux<PodcastDTO> getAllPodcasts() {
        return service.getAllPodcasts();
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get podcasts by author ID")
    public Flux<PodcastDTO> getPodcastsByAuthorId(@PathVariable UUID authorId) {
        return service.getPodcastsByAuthorId(authorId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a podcast")
    public Mono<PodcastDTO> updatePodcast(@PathVariable UUID id, @Valid @RequestBody PodcastDTO podcastDTO) {
        return service.updatePodcast(id, podcastDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a podcast")
    public Mono<Void> deletePodcast(@PathVariable UUID id) {
        return service.deletePodcast(id);
    }
}
