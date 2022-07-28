package com.guavapay.delivery.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Component
public class SecurityService {

    private final WebClient webClient;

    private final String authServer;

    public SecurityService(final WebClient webClient, @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") final String authServer) {
        this.webClient = webClient;
        this.authServer = authServer;
    }

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Optional<Long> getAuthenticatedUserId() {
        var auth = getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Jwt){
            var jwt = (Jwt) auth.getPrincipal();
            return Optional.ofNullable(jwt.getClaim("user_id"));
        }
        else {
            return Optional.empty();
        }
    }

    public CustomOidcUserInfo getUserInfo() {
        var typeReference = new ParameterizedTypeReference<Map<String, Object>>() {
        };
        return this.webClient
                .get()
                .uri(authServer + "/userinfo")
                .retrieve()
                .bodyToMono(typeReference)
                .map(CustomOidcUserInfo::new)
                .block();
    }


}