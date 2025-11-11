package ru.otus.fin.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.fin.library.entities.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

}
