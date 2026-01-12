package com.poi.yow_point.application.services.kafka;

import com.poi.yow_point.presentation.dto.event.PoiEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PoiKafkaProducer {

    private static final String TOPIC = "poi-events";
    private final KafkaTemplate<String, PoiEvent> kafkaTemplate;

    public void sendPoiEvent(PoiEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.getPoi().getPoiId().toString(), event);
            log.info("Sent POI event of type {} for POI ID {} to topic '{}'",
                    event.getType(), event.getPoi().getPoiId(), TOPIC);
        } catch (Exception e) {
            log.error("Failed to send POI event to Kafka for POI ID {}: {}",
                    event.getPoi().getPoiId(), e.getMessage());
        }
    }
}
