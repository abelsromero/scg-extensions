package com.vmware.scg.extensions;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

	public CustomHeaderGatewayFilterFactory() {
		System.out.println("123");
	}

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {

			exchange.getResponse()
					.getHeaders()
					.put("X-My-Header", List.of("Created-on-" + LocalDateTime.now()));
			return chain.filter(exchange);
		};
	}
}
