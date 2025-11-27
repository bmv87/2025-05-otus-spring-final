package ru.otus.fin.library.services;

import org.springframework.data.domain.Pageable;
import ru.otus.fin.library.controllers.BookFilterParams;
import ru.otus.fin.library.dto.books.BookAdminListItemDto;
import ru.otus.fin.library.dto.books.BookCreateDto;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.dto.common.Paginated;

public interface BookService {

    BookDto findById(long id);

    Paginated<BookDto> getList(BookFilterParams filters, Pageable pageable);

    Paginated<BookAdminListItemDto> getAdminList(BookFilterParams filters, Pageable pageable);

    BookDto insert(BookCreateDto bookDto);

    BookDto update(long id, BookCreateDto bookDto);

    void deleteById(long id);
}
