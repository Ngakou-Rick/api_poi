package com.poi.yow_point.application.service;

import com.poi.yow_point.domain.ports.in.PodcastPort;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.PodcastDTO;
import com.poi.yow_point.infrastructure.adapters.outbound.persistence.repository.PodcastRepository;
import com.poi.yow_point.infrastructure.mappers.PodcastMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PodcastServiceImpl implements PodcastPort {

    private final PodcastRepository repository;
    private final PodcastMapper mapper;

    @Override
    public Mono<PodcastDTO> createPodcast(PodcastDTO podcastDTO) {
        var entity = mapper.toEntity(mapper.toDomain(podcastDTO));
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        return repository.save(entity).map(mapper::toDto);
    }

    @Override
    public Mono<PodcastDTO> getPodcastById(UUID podcastId) {
        return repository.findById(podcastId).map(mapper::toDto);
    }

    @Override
    public Flux<PodcastDTO> getAllPodcasts() {
        return repository.findAll().map(mapper::toDto);
    }

    @Override
    public Flux<PodcastDTO> getPodcastsByAuthorId(UUID authorId) {
        return repository.findAllByAuthorId(authorId).map(mapper::toDto);
    }

    @Override
    public Mono<PodcastDTO> updatePodcast(UUID podcastId, PodcastDTO podcastDTO) {
        return repository.findById(podcastId)
                .flatMap(existing -> {
                    existing.setTitle(podcastDTO.getTitle());
                    existing.setDescription(podcastDTO.getDescription());
                    existing.setAudioUrl(podcastDTO.getAudioUrl());
                    existing.setDuration(podcastDTO.getDuration());
                    existing.setImageUrl(podcastDTO.getImageUrl());
                    existing.setTags(mapper.listToString(podcastDTO.getTags()));
                    existing.setUpdatedAt(OffsetDateTime.now());
                    return repository.save(existing);
                })
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> deletePodcast(UUID podcastId) {
        return repository.deleteById(podcastId);
    }
}
