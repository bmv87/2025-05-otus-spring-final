package ru.otus.fin.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.fin.library.entities.BookLink;
import ru.otus.fin.library.fixture.EntityGenerator;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы со ссылками на картинки книг")
@DataJpaTest
public class BookPictureRepositoryTest {

    @Autowired
    private BookPictureRepository bookPictureRepository;

    @Autowired
    private TestEntityManager em;


    @DisplayName("должен загружать список ссылок на картинки книги")
    @Test
    void shouldReturnAllBookLinks() {
        var book = EntityGenerator.getBooks().get(0);
        var expectedList = book.getPictures();
        var actualList = bookPictureRepository.findAllByBookId(book.getId());

        assertThat(actualList).isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(
                        BookLink.Fields.book,
                        BookLink.Fields.createdBy)
                .containsAll(expectedList);
    }

    @DisplayName("должен сохранять новую ссылку на картинку книги")
    @Test
    void shouldSaveNewBookLink() {
        var book = EntityGenerator.getBooks().get(0);
        var expectedItem = EntityGenerator.getBookPicture(3L, book.getId());
        expectedItem.setId(0);
        expectedItem.setBook(book);
        var actualItem = bookPictureRepository.save(expectedItem);
        assertThat(actualItem).isNotNull()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedItem);

        assertThat(bookPictureRepository.findById(actualItem.getId()))
                .isPresent()
                .get()
                .isEqualTo(actualItem);
    }

    @DisplayName("должен удалять ссылку на картинку книги по id")
    @Test
    void shouldDeleteBookLink() {
        var book = EntityGenerator.getBooks().get(0);
        var deletedItem = book.getPictures().get(0);
        assertThat(bookPictureRepository.findById(deletedItem.getId())).isPresent();
        bookPictureRepository.deleteById(deletedItem.getId());
        assertThat(bookPictureRepository.findById(deletedItem.getId())).isEmpty();
    }
}
