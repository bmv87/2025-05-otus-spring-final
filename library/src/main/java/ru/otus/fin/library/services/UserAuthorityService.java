package ru.otus.fin.library.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public interface UserAuthorityService {
    Collection<GrantedAuthority> loadAuthoritiesByUsername(String username) throws UsernameNotFoundException;
}
