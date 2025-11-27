package ru.otus.fin.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.fin.library.entities.Book;

public interface CustomBookRepository {

    Page<Book> findAllWithAuthors(Specification<Book> spec, Pageable pageable);
}
