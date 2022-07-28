package com.guavapay.authorization.config;

import com.guavapay.authorization.domain.User;
import com.guavapay.authorization.domain.UserRole;
import com.guavapay.authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthorizationInitConfig {

    private final RegisteredClientRepository registeredClientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (registeredClientRepository.findByClientId("api-client") == null) {
            RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("api-client")
                    .clientSecret(passwordEncoder.encode("secret"))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .redirectUri("https://oauth.pstmn.io/v1/callback")
                    .redirectUri("https://oidcdebugger.com/debug")
                    .redirectUri("http://127.0.0.1:8080/webjars/swagger-ui/oauth2-redirect.html")
                    .scope(OidcScopes.OPENID)
                    .scope("api.delivery")
                    .scope("api.courier")
                    .scope("api.user")
                    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                    .build();
            registeredClientRepository.save(registeredClient);
        }
        if (!userRepository.existsByUsername("yasarmatrac")) {
            User user = User.builder()
                    .name("Yaşar")
                    .surname("Matraç")
                    .username("yasarmatrac")
                    .password(passwordEncoder.encode("123456"))
                    .email("yasarmatrac@gmail.com")
                    .expired(false)
                    .locked(false)
                    .enabled(true)
                    .userRole(UserRole.ADMIN)
                    .build();
            userRepository.save(user);
        }

    }

}
