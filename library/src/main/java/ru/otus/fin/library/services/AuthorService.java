package ru.otus.fin.library.services;

import ru.otus.fin.library.dto.authors.AuthorAdminListItemDto;
import ru.otus.fin.library.dto.authors.AuthorDictionaryDto;
import ru.otus.fin.library.dto.authors.AuthorDto;

import java.util.List;

public interface AuthorService {

    List<AuthorAdminListItemDto> getAdminList(String name);

    List<AuthorDictionaryDto> getDictionaryList(String name);

    AuthorDto getById(Long id);
}
