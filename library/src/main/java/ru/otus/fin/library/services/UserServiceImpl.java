package ru.otus.fin.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.fin.library.dto.users.UserInfoDto;
import ru.otus.fin.library.exceptions.EntityNotFoundException;
import ru.otus.fin.library.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final LocalizedMessagesService localizedMessagesService;

    @Override
    public UserInfoDto getByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        localizedMessagesService.getMessage("errors.user_not_found",
                                username)));

        return new UserInfoDto(user.getFullName());
    }
}
