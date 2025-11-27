package ru.otus.fin.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.fin.library.entities.BookPicture;

import java.util.List;

public interface BookPictureRepository extends JpaRepository<BookPicture, Long> {

    boolean existsByIdAndBookId(Long linkId, Long bookId);

    List<BookPicture> findAllByBookId(Long bookId);
}
