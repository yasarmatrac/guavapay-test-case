package com.guavapay.authorization.config;

import com.guavapay.authorization.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor

public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    private final UserService userService;

    @Override
    public void customize(JwtEncodingContext context) {
        context.getClaims().claim("grant_type", context.getTokenType());
        if (context.getTokenType() == OAuth2TokenType.ACCESS_TOKEN) {
            Authentication principal = context.getPrincipal();
            context.getClaims().claim("role", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
            userService.findByUsername(principal.getName()).ifPresent(x -> context.getClaims().claim("user_id", x.getId()));
        }
    }
}
