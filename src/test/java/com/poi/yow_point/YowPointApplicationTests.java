package com.poi.yow_point;

import com.poi.yow_point.infrastructure.elasticsearch.repository.PoiDocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class YowPointApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	// Mock the Elasticsearch repository to prevent connection issues during tests
	@MockBean
	private PoiDocumentRepository poiDocumentRepository;

	@Test
	void contextLoads() {
		webTestClient.get().uri("/actuator/health")
				.exchange()
				.expectStatus().isOk();
	}
}
