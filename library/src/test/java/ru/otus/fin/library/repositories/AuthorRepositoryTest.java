package ru.otus.fin.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.fin.library.entities.Author;
import ru.otus.fin.library.fixture.EntityGenerator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами")
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать весь список авторов")
    @Test
    void shouldReturnAllAuthors() {
        var expectedList = EntityGenerator.getAuthors();
        var actualList = authorRepository.findAll();
        assertThat(actualList).isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Author.Fields.createdBy)
                .containsAll(expectedList);
    }

    @DisplayName("должен загружать список, отфильтрованный по имени автора")
    @Test
    void shouldReturnFilteredAuthors() {
        var author = EntityGenerator.getAuthor(1L);
        var expectedList = List.of(author);
        var actualList = authorRepository.findByFullNameContainsIgnoreCase(author.getFullName());
        assertThat(actualList).isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Author.Fields.createdBy)
                .containsAll(expectedList);
    }
}