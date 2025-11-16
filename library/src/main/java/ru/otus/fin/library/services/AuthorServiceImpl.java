package ru.otus.fin.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.fin.library.dto.authors.AuthorAdminListItemDto;
import ru.otus.fin.library.dto.authors.AuthorDictionaryDto;
import ru.otus.fin.library.dto.authors.AuthorDto;
import ru.otus.fin.library.entities.Author;
import ru.otus.fin.library.exceptions.EntityNotFoundException;
import ru.otus.fin.library.mappers.AuthorMapper;
import ru.otus.fin.library.repositories.AuthorRepository;
import ru.otus.fin.library.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final UserRepository userRepository;

    private final LocalizedMessagesService localizedMessagesService;

    @Override
    public List<AuthorAdminListItemDto> getAdminList(String name) {
        List<Author> authors = findAllOrByName(name);
        return authorMapper.mapToAdminListDto(authors);
    }

    @Override
    public List<AuthorDictionaryDto> getDictionaryList(String name) {
        List<Author> authors = findAllOrByName(name);
        return authorMapper.mapToDictionaryListDto(authors);
    }

    @Override
    public AuthorDto getById(Long id) {
        var author = authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        localizedMessagesService.getMessage("errors.author_not_found", id)));
        return authorMapper.mapToDto(author);
    }

    private List<Author> findAllOrByName(String name) {
        if (StringUtils.hasText(name)) {
            return authorRepository.findByFullNameContainsIgnoreCase(name);
        }
        return authorRepository.findAll();
    }
}
