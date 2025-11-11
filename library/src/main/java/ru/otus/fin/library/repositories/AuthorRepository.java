package ru.otus.fin.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.fin.library.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
