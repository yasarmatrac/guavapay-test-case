package com.guavapay.user.security;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.List;
import java.util.Map;

public class CustomOidcUserInfo extends OidcUserInfo {


    public CustomOidcUserInfo(Map<String, Object> claims) {
        super(claims);
    }

    public String getUserId() {
        return this.getClaimAsString(CustomStandardClaimNames.USER_ID);
    }

    public List<String> getRoles() {
        return this.getClaimAsStringList(CustomStandardClaimNames.ROLE);
    }


    public static interface CustomStandardClaimNames {
        String USER_ID = "user_id";
        String ROLE = "role";
    }
}
