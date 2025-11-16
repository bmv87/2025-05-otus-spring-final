package ru.otus.fin.library.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.fin.library.dto.books.BookAdminListItemDto;
import ru.otus.fin.library.dto.books.BookCreateDto;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.entities.Book;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final SectionMapper sectionMapper;

    public Book mapToEntity(BookCreateDto dto) {
        var book = new Book();
        book.setId(0);
        book.setTitle(dto.getTitle());
        book.setDescription(dto.getDescription());
        book.setPublicationYear(dto.getPublicationYear());
        return book;
    }

    public void mapToEntity(BookCreateDto dto, Book entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPublicationYear(dto.getPublicationYear());
    }

    public BookDto mapToDto(Book entity) {
        var book = new BookDto();
        book.setId(entity.getId());
        book.setTitle(entity.getTitle());
        book.setDescription(entity.getDescription());
        book.setPublicationYear(entity.getPublicationYear());
        book.setSections(sectionMapper.mapToDictionaryListDto(entity.getSections()));
        book.setAuthors(authorMapper.mapToDictionaryListDto(entity.getAuthors()));

        return book;
    }

    public BookAdminListItemDto mapToAdminListItemDto(Book entity) {
        var book = new BookAdminListItemDto();
        book.setId(entity.getId());
        book.setTitle(entity.getTitle());
        book.setDescription(entity.getDescription());
        book.setPublicationYear(entity.getPublicationYear());
        book.setSections(sectionMapper.mapToDictionaryListDto(entity.getSections()));
        book.setAuthors(authorMapper.mapToDictionaryListDto(entity.getAuthors()));
        book.setCreatedBy(entity.getCreatedBy().getFullName());

        return book;
    }

    public List<BookDto> mapToListDto(List<Book> entities) {
        return entities.stream().map(this::mapToDto).toList();
    }

    public List<BookAdminListItemDto> mapToAdminListDto(List<Book> entities) {
        return entities.stream().map(this::mapToAdminListItemDto).toList();
    }
}
