package com.guavapay.authorization.domain;

import lombok.Data;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "`authorization`")
public class Authorization {
    @Id
    @Column
    private String id;
    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;
    @Column(length = 4000)
    private String attributes;
    @Column(length = 500)
    private String state;

    @Column(length = 4000)
    private String authorizationCodeValue;
    private Instant authorizationCodeIssuedAt;
    private Instant authorizationCodeExpiresAt;
    private String authorizationCodeMetadata;

    @Column(length = 4000)
    private String accessTokenValue;
    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;
    @Column(length = 2000)
    private String accessTokenMetadata;
    private String accessTokenType;
    @Column(length = 1000)
    private String accessTokenScopes;

    @Column(length = 4000)
    private String refreshTokenValue;
    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;
    @Column(length = 2000)
    private String refreshTokenMetadata;

    @Column(length = 4000)
    private String oidcIdTokenValue;
    private Instant oidcIdTokenIssuedAt;
    private Instant oidcIdTokenExpiresAt;
    @Column(length = 2000)
    private String oidcIdTokenMetadata;
    @Column(length = 2000)
    private String oidcIdTokenClaims;

}

