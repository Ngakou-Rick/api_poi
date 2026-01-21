package com.poi.yow_point.domain.ports.in;

import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.PodcastDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PodcastPort {
    Mono<PodcastDTO> createPodcast(PodcastDTO podcastDTO);
    Mono<PodcastDTO> getPodcastById(UUID podcastId);
    Flux<PodcastDTO> getAllPodcasts();
    Flux<PodcastDTO> getPodcastsByAuthorId(UUID authorId);
    Mono<PodcastDTO> updatePodcast(UUID podcastId, PodcastDTO podcastDTO);
    Mono<Void> deletePodcast(UUID podcastId);
}
