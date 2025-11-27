package ru.otus.fin.library.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;
import ru.otus.fin.library.services.UserAuthorityService;

import java.util.Collection;

public class JwtToDaoGrantedAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private UserAuthorityService userAuthorityService;

    private String principalClaimName = StandardClaimNames.PREFERRED_USERNAME;

    public JwtToDaoGrantedAuthorityConverter(UserAuthorityService userAuthorityService) {
        this.userAuthorityService = userAuthorityService;
    }

    @NotNull
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        String principalClaimValue = jwt.getClaimAsString(principalClaimName);
        var authorities = userAuthorityService.loadAuthoritiesByUsername(principalClaimValue);
        return authorities;
    }

    public void setPrincipalClaimName(String principalClaimName) {
        Assert.hasText(principalClaimName, "principalClaimName cannot be empty");
        this.principalClaimName = principalClaimName;
    }
}
