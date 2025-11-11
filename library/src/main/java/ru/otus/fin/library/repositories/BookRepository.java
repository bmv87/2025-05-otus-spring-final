package ru.otus.fin.library.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.fin.library.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(value = "books-with-pictures-authors-sections-entity-graph")
    Optional<Book> findById(Long id);

    @EntityGraph(value = "books-authors-sections-entity-graph")
    List<Book> findAll();
}

