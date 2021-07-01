package com.vmware.scg.extensions;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UrlContainsPredicateTest {

	final WireMockServer wireMock = new WireMockServer(9090);

	@Autowired
	WebTestClient webTestClient;

	@BeforeAll
	void setUp() {
		wireMock.stubFor(get(anyUrl()).willReturn(ok()));
		wireMock.start();
	}

	@AfterAll
	void tearDown() {
		wireMock.stop();
	}

	@Test
	void should_apply_extension_predicate_and_match() {
		webTestClient
				.get()
				.uri("/contains?magic-word=its-me")
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	void should_apply_extension_predicate_and_not_match() {
		webTestClient
				.get()
				.uri("/contains?magic-word=nothing-to-see")
				.exchange()
				.expectStatus()
				.isNotFound();
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
