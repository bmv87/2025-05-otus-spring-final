package ru.otus.fin.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import ru.otus.fin.library.security.CustomAccessDeniedHandler;
import ru.otus.fin.library.security.JwtToDaoGrantedAuthorityConverter;
import ru.otus.fin.library.services.UserAuthorityService;

import static ru.otus.fin.library.security.AuthorityConstants.ADMIN;
import static ru.otus.fin.library.security.AuthorityConstants.READER;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserAuthorityService userAuthorityService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> {
                    config.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler()))
                .authorizeHttpRequests(config -> {
                    config
                            .requestMatchers("/actuator/info").permitAll()
                            .requestMatchers("/actuator/**").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers("/api/v1/admin/**").hasRole(ADMIN)
                            .requestMatchers(HttpMethod.GET, "/api/v1/books/{bookId}/links").hasAnyRole(ADMIN, READER)
                            .requestMatchers(HttpMethod.GET, "/api/v1/users/current").authenticated()
                            .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll();
                })
                .build();
    }

    @Bean
    JwtAuthenticationConverter authenticationConverter() {
        var authenticationConverter = new JwtAuthenticationConverter();
        var authoritiesConverter = new JwtToDaoGrantedAuthorityConverter(userAuthorityService);
        authenticationConverter.setPrincipalClaimName(StandardClaimNames.PREFERRED_USERNAME);
        authoritiesConverter.setPrincipalClaimName(StandardClaimNames.PREFERRED_USERNAME);
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return authenticationConverter;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
