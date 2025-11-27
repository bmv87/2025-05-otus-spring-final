package ru.otus.fin.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.fin.library.entities.BookLink;

import java.util.List;

public interface BookLinkRepository extends JpaRepository<BookLink, Long> {

    boolean existsByIdAndBookId(Long linkId, Long bookId);

    List<BookLink> findAllByBookId(Long bookId);
}
