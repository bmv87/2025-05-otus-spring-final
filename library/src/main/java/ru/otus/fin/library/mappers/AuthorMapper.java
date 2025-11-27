package ru.otus.fin.library.mappers;

import org.springframework.stereotype.Component;
import ru.otus.fin.library.dto.authors.AuthorAdminListItemDto;
import ru.otus.fin.library.dto.authors.AuthorDictionaryDto;
import ru.otus.fin.library.dto.authors.AuthorDto;
import ru.otus.fin.library.entities.Author;

import java.util.List;

@Component
public class AuthorMapper {

    public AuthorDictionaryDto mapToDictionaryDto(Author entity) {
        var author = new AuthorDictionaryDto();
        author.setId(entity.getId());
        author.setName(entity.getFullName());
        return author;
    }

    public AuthorDto mapToDto(Author entity) {
        var author = new AuthorDto();
        author.setId(entity.getId());
        author.setFullName(entity.getFullName());
        author.setDescription(entity.getDescription());
        return author;
    }

    public AuthorAdminListItemDto mapToListItemDto(Author entity) {
        var author = new AuthorAdminListItemDto();
        author.setId(entity.getId());
        author.setFullName(entity.getFullName());
        author.setDescription(entity.getDescription());
        author.setCreatedBy(entity.getCreatedBy().getFullName());
        return author;
    }

    public List<AuthorAdminListItemDto> mapToAdminListDto(List<Author> entities) {
        return entities.stream().map(this::mapToListItemDto).toList();
    }

    public List<AuthorDictionaryDto> mapToDictionaryListDto(List<Author> entities) {
        return entities.stream().map(this::mapToDictionaryDto).toList();
    }
}
