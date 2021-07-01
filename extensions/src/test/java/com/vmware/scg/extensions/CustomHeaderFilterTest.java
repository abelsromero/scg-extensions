package com.vmware.scg.extensions;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
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

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomHeaderFilterTest {

	final WireMockServer wireMock = new WireMockServer(9090);

	@Autowired
	WebTestClient webTestClient;

	@BeforeAll
	void setUp() {
		wireMock.stubFor(get("/add-header").willReturn(ok()));
		wireMock.start();
	}

	@AfterAll
	void tearDown() {
		wireMock.stop();
	}

	@Test
	void should_apply_extension_filter() {
		webTestClient
				.get()
				.uri("/add-header")
				.exchange()
				.expectStatus()
				.isOk();

		wireMock.verify(getRequestedFor(urlPathEqualTo("/add-header"))
				.withHeader("X-My-Header", new EqualToPattern("SGVsbG8gd29ybGQh")));
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
