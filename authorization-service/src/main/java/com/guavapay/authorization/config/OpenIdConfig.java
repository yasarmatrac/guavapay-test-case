package com.guavapay.authorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;

@Configuration
public class OpenIdConfig {


    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().issuer("http://authorization-service:9000").build();
    }

}
