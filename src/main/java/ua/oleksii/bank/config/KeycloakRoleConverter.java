package ua.oleksii.bank.config;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        var roles = (ArrayList<String>) source.getClaims().get("roles");
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>();
        }
        return roles.stream()
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
