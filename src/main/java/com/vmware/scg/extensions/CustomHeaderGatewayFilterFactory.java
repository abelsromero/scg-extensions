package com.vmware.scg.extensions;

import java.time.LocalDateTime;
import java.util.List;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

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
					.put("X-My-Header", List.of("Created-on-" + LocalDateTime.now()));
			return chain.filter(exchange);
		};
	}
}
