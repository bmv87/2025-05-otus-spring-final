package ru.otus.fin.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.fin.library.entities.Author;
import ru.otus.fin.library.entities.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {

}
