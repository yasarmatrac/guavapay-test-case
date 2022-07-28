package com.guavapay.courier.security;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomOidcUserInfo extends OidcUserInfo {


    public CustomOidcUserInfo(Map<String, Object> claims) {
        super(claims);
    }

    public Long getUserId() {
        return Long.valueOf(this.getClaimAsString(CustomStandardClaimNames.USER_ID));
    }

    public List<UserRole> getRoles() {
        return this.getClaimAsStringList(CustomStandardClaimNames.ROLE).stream().map(UserRole::valueOf).collect(Collectors.toList());
    }


    public static interface CustomStandardClaimNames {
        String USER_ID = "user_id";
        String ROLE = "role";
    }

    public enum UserRole {
        ADMIN, COURIER, USER
    }
}
