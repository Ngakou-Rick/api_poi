package com.poi.yow_point;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.reactive.WebFluxSecurityConfiguration"
})
@AutoConfigureWebTestClient
class YowPointApplicationTests {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	void contextLoads() {
		webTestClient.get().uri("/actuator/health")
				.exchange()
				.expectStatus().isOk();
	}
}
