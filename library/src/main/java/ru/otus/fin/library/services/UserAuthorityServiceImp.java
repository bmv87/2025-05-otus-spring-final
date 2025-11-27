package ru.otus.fin.library.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.otus.fin.library.entities.UserEntity;
import ru.otus.fin.library.repositories.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserAuthorityServiceImp implements UserAuthorityService {

    private final UserRepository userRepository;

    private final String rolePrefix = "ROLE_";

    @Override
    @Transactional
    public Collection<GrantedAuthority> loadAuthoritiesByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority(rolePrefix + r.getName()))
                .collect(Collectors.toUnmodifiableList());
    }
}
