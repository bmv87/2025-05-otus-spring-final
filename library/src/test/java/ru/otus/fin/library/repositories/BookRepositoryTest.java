package ru.otus.fin.library.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.otus.fin.library.entities.Book;
import ru.otus.fin.library.fixture.EntityGenerator;
import ru.otus.fin.library.repositories.specifications.SpecificationsHelper;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами")
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookLinkRepository bookLinkRepository;

    @Autowired
    private BookPictureRepository bookPictureRepository;

    @Autowired
    private TestEntityManager em;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = EntityGenerator.getBooks();
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("ru.otus.fin.library.fixture.EntityGenerator#getBooks")
    void shouldReturnCorrectBookById(Book expectedItem) {
        var actualItem = bookRepository.findById(expectedItem.getId());
        assertThat(actualItem)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields(
                        Book.Fields.createdBy,
                        Book.Fields.sections,
                        Book.Fields.links,
                        Book.Fields.pictures
                )
                .ignoringFieldsMatchingRegexes(".*.createdBy")
                .isEqualTo(expectedItem);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var expectedItems = dbBooks;
        var pageable = PageRequest.of(0, dbBooks.size(), Sort.by(Book.Fields.id).ascending());
        var actualBooks = bookRepository.findAll(SpecificationsHelper.and(new ArrayList<>()), pageable);
        assertThat(actualBooks.getContent())
                .isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                        Book.Fields.createdBy,
                        Book.Fields.sections,
                        Book.Fields.authors,
                        Book.Fields.links,
                        Book.Fields.pictures
                )
                .containsAll(expectedItems);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedItem = EntityGenerator.getBook(4L, List.of(1L, 2L), List.of(1L, 2L), List.of(), List.of());
        expectedItem.setId(0);
        var actualItem = bookRepository.save(expectedItem);
        assertThat(actualItem).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedItem);

        assertThat(bookRepository.findById(actualItem.getId()))
                .isPresent()
                .get()
                .isEqualTo(actualItem);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedItem = dbBooks.get(0);

        assertThat(bookRepository.findById(expectedItem.getId()))
                .isPresent();
        expectedItem.setTitle("New_book_title");
        expectedItem.setDescription("New_book_description");
        expectedItem.setPublicationYear(1200);
        expectedItem.setSections(List.of(expectedItem.getSections().get(0)));
        expectedItem.setAuthors(List.of(expectedItem.getAuthors().get(0)));
        expectedItem.setAuthors(List.of(expectedItem.getAuthors().get(0)));
        var actualItem = bookRepository.save(expectedItem);
        assertThat(actualItem).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedItem);

        assertThat(bookRepository.findById(actualItem.getId()))
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(actualItem);
    }

    @DisplayName("должен удалять книгу по id и все ссылки на книги и картинки")
    @Test
    void shouldDeleteBookWithAllLinks() {
        var deletedItem = dbBooks.get(0);
        assertThat(bookRepository.findById(deletedItem.getId())).isPresent();
        assertThat(bookLinkRepository.findById(deletedItem.getLinks().get(0).getId())).isPresent();
        assertThat(bookPictureRepository.findById(deletedItem.getPictures().get(0).getId())).isPresent();
        bookRepository.deleteById(deletedItem.getId());
        assertThat(bookRepository.findById(deletedItem.getId())).isEmpty();
        assertThat(bookLinkRepository.findById(deletedItem.getLinks().get(0).getId())).isEmpty();
        assertThat(bookPictureRepository.findById(deletedItem.getPictures().get(0).getId())).isEmpty();
    }
}