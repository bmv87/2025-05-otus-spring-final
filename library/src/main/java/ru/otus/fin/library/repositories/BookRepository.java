package ru.otus.fin.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.fin.library.entities.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Override
    @EntityGraph(value = "books-authors-sections-entity-graph")
    Optional<Book> findById(Long id);

    @EntityGraph(value = "books-authors-sections-entity-graph")
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);
}

