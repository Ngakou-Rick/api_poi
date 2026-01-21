package com.poi.yow_point.infrastructure.adapters.inbound.rest;

import com.poi.yow_point.domain.ports.in.PointOfInterestPort;
import com.poi.yow_point.infrastructure.adapters.inbound.rest.dto.PointOfInterestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PointOfInterestController.class)
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.reactive.WebFluxSecurityConfiguration,org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration,org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration"
})
@Import(GlobalExceptionHandler.class)
class PointOfInterestControllerTest {

    @SpringBootApplication(scanBasePackages = "com.poi.yow_point.infrastructure.adapters.inbound.rest")
    static class TestConfig {
    }

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PointOfInterestPort poiService;

    @Test
    void getAllPois_shouldReturnList() {
        UUID poiId = UUID.randomUUID();
        PointOfInterestDTO dto = PointOfInterestDTO.builder()
                .poiId(poiId)
                .poiName("Test POI")
                .build();

        when(poiService.findAll()).thenReturn(Flux.just(dto));

        webTestClient.get()
                .uri("/api/v1/pois")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(PointOfInterestDTO.class)
                .hasSize(1)
                .contains(dto);
    }

    @Test
    void getAllPois_shouldReturnError() {
        when(poiService.findAll()).thenReturn(Flux.error(new RuntimeException("Database error")));

        webTestClient.get()
                .uri("/api/v1/pois")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Database error");
    }
}
