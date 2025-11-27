package ru.otus.fin.library.services;

import ru.otus.fin.library.dto.users.UserInfoDto;

public interface UserService {
    UserInfoDto getByUsername(String username);
}
