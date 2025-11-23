package ru.otus.fin.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.fin.library.entities.BookLink;
import ru.otus.fin.library.fixture.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы со ссылками на файлы книг")
@DataJpaTest
public class BookLinkRepositoryTest {

    @Autowired
    private BookLinkRepository bookLinkRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен загружать список ссылок на файл книги")
    @Test
    void shouldReturnAllBookLinks() {
        var book = EntityGenerator.getBooks().get(0);
        var expectedList = book.getLinks();
        var actualList = bookLinkRepository.findAllByBookId(book.getId());

        assertThat(actualList).isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                        BookLink.Fields.book,
                        BookLink.Fields.createdBy)
                .containsAll(expectedList);
    }

    @DisplayName("должен сохранять новую ссылку на файл книги")
    @Test
    void shouldSaveNewBookLink() {
        var book = EntityGenerator.getBooks().get(0);
        var expectedItem = EntityGenerator.getBookLink(4L, book.getId());
        expectedItem.setId(0);
        expectedItem.setBook(book);
        var actualItem = bookLinkRepository.save(expectedItem);
        assertThat(actualItem).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedItem);

        assertThat(bookLinkRepository.findById(actualItem.getId()))
                .isPresent()
                .get()
                .isEqualTo(actualItem);
    }

    @DisplayName("должен удалять ссылку на файл книги по id")
    @Test
    void shouldDeleteBookLink() {
        var book = EntityGenerator.getBooks().get(0);
        var deletedItem = book.getLinks().get(0);
        assertThat(bookLinkRepository.findById(deletedItem.getId())).isPresent();
        bookLinkRepository.deleteById(deletedItem.getId());
        assertThat(bookLinkRepository.findById(deletedItem.getId())).isEmpty();
    }
}
