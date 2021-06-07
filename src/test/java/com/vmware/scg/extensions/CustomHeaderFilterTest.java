package com.vmware.scg.extensions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomHeaderFilterTest {

	@LocalServerPort
	int gatewayPort;

	WebTestClient webTestClient;

	@BeforeAll
	void setUp() {
		this.webTestClient = WebTestClient
				.bindToServer()
				.baseUrl("http://localhost:" + gatewayPort)
				.build();
	}

	@Test
	void should_apply_extension_filter() {
		webTestClient
				.get()
				.uri("/get")
				.exchange()
				.expectStatus()
				.isOk()
				.expectHeader()
				.valuesMatch("X-My-Header", "Created-on-.+");
	}

	@SpringBootApplication
	public static class GatewayApplication {

		@Bean
		public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
			http.authorizeExchange().anyExchange().permitAll();
			return http.build();
		}

		public static void main(String[] args) {
			SpringApplication.run(GatewayApplication.class, args);
		}
	}
}
