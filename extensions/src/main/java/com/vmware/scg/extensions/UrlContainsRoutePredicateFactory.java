package com.vmware.scg.extensions;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

@Component
public class UrlContainsRoutePredicateFactory extends AbstractRoutePredicateFactory<UrlContainsRoutePredicateFactory.PredicateConfig> {

	private static final Logger LOG = LoggerFactory.getLogger(UrlContainsRoutePredicateFactory.class);

	public UrlContainsRoutePredicateFactory() {
		super(PredicateConfig.class);
	}

	@Override
	public Predicate<ServerWebExchange> apply(PredicateConfig config) {
		return (GatewayPredicate) serverWebExchange -> {
			if (!StringUtils.hasText(config.pattern))
				return false;

			String url = serverWebExchange.getRequest().getURI().toASCIIString();
			LOG.info("Testing url: " + url + " for " + config.pattern);
			return url.contains(config.pattern);
		};
	}

	@Override
	public List<String> shortcutFieldOrder() {
		return List.of("pattern");
	}

	@Override
	public String name() {
		return "UrlContains";
	}

	@Validated
	public static class PredicateConfig {

		@NotBlank
		private String pattern;

		public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}
	}
}