package com.guavapay.user.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;

import java.util.*;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (jwt.getClaims().containsKey("role")) {
            grantedAuthorities.addAll(getAuthorities(jwt, "role", "ROLE_"));
        }
        if (jwt.getClaims().containsKey("scope")) {
            grantedAuthorities.addAll(getAuthorities(jwt, "scope", "SCOPE_"));
        }
        return grantedAuthorities;
    }

    private Collection<GrantedAuthority> getAuthorities(Jwt jwt, String claimName, String prefix) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : getAuthorities(jwt, claimName)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(prefix + authority));
        }
        return grantedAuthorities;
    }

    private Collection<String> getAuthorities(Jwt jwt, String claimName) {
        Object authorities = jwt.getClaim(claimName);
        if (authorities instanceof String) {
            if (StringUtils.hasText((String) authorities)) {
                return Arrays.asList(((String) authorities).split(" "));
            }
            return Collections.emptyList();
        }
        if (authorities instanceof Collection) {
            return castAuthoritiesToCollection(authorities);
        }
        return Collections.emptyList();
    }

    private Collection<String> castAuthoritiesToCollection(Object authorities) {
        return (Collection<String>) authorities;
    }
}
