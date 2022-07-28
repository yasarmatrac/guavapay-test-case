package com.guavapay.delivery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServerBearerExchangeFilterFunction;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient courierWebClient(final @Value("${application.client.courier.base-url}") String courierServiceUrl) {
        return WebClient.builder().baseUrl(courierServiceUrl)
                .filter(new ServletBearerExchangeFilterFunction())
                .filter(new ServerBearerExchangeFilterFunction())
                .build();
    }


}