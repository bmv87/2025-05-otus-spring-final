package ru.otus.fin.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.fin.library.entities.Section;
import ru.otus.fin.library.fixture.EntityGenerator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с разделами ")
@DataJpaTest
class SectionRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать весь список разделов")
    @Test
    void shouldReturnAllSections() {
        var expectedList = EntityGenerator.getSections();
        var actualList = sectionRepository.findAll();
        assertThat(actualList).isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Section.Fields.createdBy)
                .containsAll(expectedList);
    }

    @DisplayName("должен загружать список, отфильтрованный по имени раздела")
    @Test
    void shouldReturnFilteredSections() {
        var item = EntityGenerator.getSection(1L);
        var expectedList = List.of(item);
        var actualList = sectionRepository.findByNameContainsIgnoreCase(item.getName());
        assertThat(actualList).isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Section.Fields.createdBy)
                .containsAll(expectedList);
    }
}