package com.guavapay.authorization.config;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    private final ProviderSettings providerSettings;
    private final CorsConfigurationSource corsConfigurationSource;

    Function<OidcUserInfoAuthenticationContext, OidcUserInfo> userInfoMapper = context -> {
        OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
        JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
        return OidcUserInfo.builder()
                .claims(stringObjectMap -> stringObjectMap.putAll(principal.getToken().getClaims()))
                .build();
    };

    public AuthorizationServerConfig(ProviderSettings providerSettings, CorsConfigurationSource corsConfigurationSource) {
        this.providerSettings = providerSettings;
        this.corsConfigurationSource = corsConfigurationSource;
    }


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

        RequestMatcher endpointsMatcher = new RequestMatcher() {
            AntPathRequestMatcher rm = new AntPathRequestMatcher(
                    providerSettings.getTokenEndpoint(), HttpMethod.OPTIONS.name());

            @Override
            public boolean matches(HttpServletRequest request) {
                return authorizationServerConfigurer.getEndpointsMatcher().matches(request) ||
                        rm.matches(request);
            }
        };
        http.requestMatcher(endpointsMatcher)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .apply(authorizationServerConfigurer)
                .oidc(oidc -> oidc.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userInfoMapper(userInfoMapper)));
        return http.formLogin(Customizer.withDefaults()).build();
    }


    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }


}