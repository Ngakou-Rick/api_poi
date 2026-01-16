package com.poi.yow_point.infrastructure.adapters.inbound.websocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poi.yow_point.application.service.PoiEventPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class PoiWebSocketHandler implements WebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(PoiWebSocketHandler.class);

    private final PoiEventPublisher eventPublisher = new PoiEventPublisher();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        log.info("WebSocket session started: {}", session.getId());

        return session.send(eventPublisher.getPublisher()
                .flatMap(poiEvent -> {
                    try {
                        String json = objectMapper.writeValueAsString(poiEvent);
                        return Mono.just(session.textMessage(json));
                    } catch (JsonProcessingException e) {
                        log.error("Error serializing POI event", e);
                        return Mono.empty();
                    }
                }))
                .doOnTerminate(() -> log.info("WebSocket session terminated: {}", session.getId()));
    }
}