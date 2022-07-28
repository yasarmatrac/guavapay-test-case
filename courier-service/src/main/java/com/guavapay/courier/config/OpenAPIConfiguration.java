package com.guavapay.courier.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenAPIConfiguration {

    @Value("${application.springdoc.service-url}")
    private String serviceUrl;

    @Value("${application.springdoc.security.token-url}")
    private String tokenUrl;

    @Value("${application.springdoc.security.authorization-url}")
    private String authorizationUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(serviceUrl))
                .components(new Components()
                        .addSecuritySchemes("Oauth2", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .bearerFormat("JWT")
                                .flows(new OAuthFlows()
                                        .authorizationCode(new OAuthFlow()
                                                .tokenUrl(tokenUrl)
                                                .authorizationUrl(authorizationUrl)
                                                .scopes(new Scopes()
                                                        .addString("api.courier", "courier service operation")
                                                        .addString("api.user", "user service operation")
                                                        .addString("api.delivery", "delivery service operation")
                                                ))))
                )
                .security(Collections.singletonList(
                        new SecurityRequirement().addList("Oauth2")))
                .info(new Info()
                        .title("Courier Service API")
                        .contact(new Contact().email("yasarmatrac@gmail.com").name("Developer: Yaşar Matraç"))
                        .license(new License().name("GNU"))
                        .version("1.0.0")
                );
    }


}