package com.vmware.scg.extensions;

import java.time.LocalDateTime;
import java.util.List;

import my.extension.encoder.HeaderEncoder;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class CustomHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {

//			Mono.just(1)
//				.publishOn(Schedulers.parallel())
//				.doOnNext(it -> {
//					try {
//						Thread.sleep(10);
//					}
//					catch (InterruptedException e) {
//						throw new RuntimeException(e);
//					}
//				})
//				.block();

			exchange.getResponse()
					.getHeaders()
					.put("X-My-Header", List.of(
							new HeaderEncoder().encode("Hello world!"),
							"Created-on-" + LocalDateTime.now()
					));
			return chain.filter(exchange);
		};
	}
}
