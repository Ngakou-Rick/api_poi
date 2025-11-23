package com.poi.yow_point.application.services.kafka;

import com.poi.yow_point.application.mappers.PoiDocumentMapper;
import com.poi.yow_point.infrastructure.elasticsearch.document.PoiDocument;
import com.poi.yow_point.infrastructure.elasticsearch.repository.PoiDocumentRepository;
import com.poi.yow_point.presentation.dto.event.PoiEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class PoiKafkaConsumer {

    private final PoiDocumentRepository elasticsearchRepository;
    private final PoiDocumentMapper poiDocumentMapper;

    @KafkaListener(topics = "poi-events", groupId = "poi-group")
    public void consumePoiEvent(PoiEvent event) {
        log.info("Received POI event of type {} for POI ID {}", event.getType(), event.getPoi().getPoiId());
        PoiDocument document = poiDocumentMapper.toDocument(event.getPoi());

        Mono<PoiDocument> operation = switch (event.getType()) {
            case CREATED, UPDATED -> elasticsearchRepository.save(document);
            case DELETED -> elasticsearchRepository.deleteById(document.getId()).then(Mono.empty());
        };

        operation.subscribe(
            savedDoc -> log.info("Successfully indexed document for POI ID {}", savedDoc.getId()),
            error -> log.error("Failed to index document for POI ID {}: {}", document.getId(), error.getMessage())
        );
    }
}
