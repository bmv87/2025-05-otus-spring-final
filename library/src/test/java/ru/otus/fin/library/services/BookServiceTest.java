package ru.otus.fin.library.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.fin.library.LibraryApplication;
import ru.otus.fin.library.controllers.BookFilterParams;
import ru.otus.fin.library.dto.books.BookCreateDto;
import ru.otus.fin.library.entities.Author;
import ru.otus.fin.library.entities.Book;
import ru.otus.fin.library.entities.Section;
import ru.otus.fin.library.exceptions.BusinessValidationException;
import ru.otus.fin.library.fixture.EntityGenerator;
import ru.otus.fin.library.mappers.BookMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.otus.fin.library.security.AuthorityConstants.ADMIN;
import static ru.otus.fin.library.security.AuthorityConstants.READER;

@DisplayName("Сервис для работы с книгами")
@SpringBootTest(
        classes = {
                BookMapper.class, BookServiceImpl.class, LibraryApplication.class
        }
)
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceTest {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookServiceImpl bookService;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbBooks = EntityGenerator.getBooks();
    }

    @DisplayName("Возвращать список всех книг для администратора с настройками фильтров и пагинации")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void shouldFindBooksWithFiltersAndPaginationForAdmin() {
        var book = dbBooks.get(0);
        var expectedList = List.of(bookMapper.mapToAdminListItemDto(book));
        var pageable = PageRequest.of(0, dbBooks.size(), Sort.by(Book.Fields.id).ascending());
        var filterParams = new BookFilterParams();
        filterParams.setTitle(book.getTitle());
        filterParams.setDescription(book.getDescription());
        filterParams.setAuthor(book.getAuthors().get(0).getId());
        filterParams.setSection(book.getSections().get(0).getId());
        filterParams.setYearEnd(book.getPublicationYear());
        filterParams.setYearStart(book.getPublicationYear());

        var actualBooks = bookService.getAdminList(filterParams, pageable);
        assertThat(actualBooks)
                .isNotNull();
        assertThat(actualBooks.getTotalCount())
                .isEqualTo(1);
        assertThat(actualBooks.getItems())
                .isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
                .containsAll(expectedList);
    }

    @DisplayName("Возвращать список всех книг для любого пользователя с настройками фильтров и пагинации")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void shouldFindBooksWithFiltersAndPaginationForAnyUsers() {
        var book = dbBooks.get(0);
        var expectedList = List.of(bookMapper.mapToDto(book));
        var pageable = PageRequest.of(0, dbBooks.size(), Sort.by(Book.Fields.id).ascending());
        var filterParams = new BookFilterParams();
        filterParams.setTitle(book.getTitle());
        filterParams.setDescription(book.getDescription());
        filterParams.setAuthor(book.getAuthors().get(0).getId());
        filterParams.setSection(book.getSections().get(0).getId());
        filterParams.setYearEnd(book.getPublicationYear());
        filterParams.setYearStart(book.getPublicationYear());

        var actualBooks = bookService.getList(filterParams, pageable);
        assertThat(actualBooks)
                .isNotNull();
        assertThat(actualBooks.getTotalCount())
                .isEqualTo(1);
        assertThat(actualBooks.getItems())
                .isNotEmpty()
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields()
                .containsAll(expectedList);
    }

    @DisplayName("Возвращать книгу для просмотра")
    @Test
    void shouldFindBookForAnyUsers() {
        var book = dbBooks.get(0);
        var expectedItem = bookMapper.mapToDto(book);

        var actualItem = bookService.findById(book.getId());

        assertThat(actualItem)
                .usingRecursiveComparison()
                .isEqualTo(expectedItem);
    }

    @DisplayName("При добавлении и редактировании вернет ошибку валидации. Год публикации не может быть из будущего")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void validateThrowBusinessValidationException() {
        var book = dbBooks.get(0);
        var expectedItem = new BookCreateDto();
        expectedItem.setTitle(book.getTitle());
        expectedItem.setDescription(book.getDescription());
        expectedItem.setAuthors(book.getAuthors().stream().map(Author::getId).toList());
        expectedItem.setSections(book.getSections().stream().map(Section::getId).toList());
        expectedItem.setPublicationYear(3000);
        assertThatThrownBy(() -> {
            bookService.insert(expectedItem);
        }).isInstanceOf(BusinessValidationException.class);
        assertThatThrownBy(() -> {
            bookService.update(1L, expectedItem);
        }).isInstanceOf(BusinessValidationException.class);
    }

    @DisplayName("Читатель не может редактировать/создавать/удалять книги")
    @Test
    @WithMockUser(username = "reader1", roles = {READER})
    void createUpdateDeleteAuthorizationDeniedExceptionForReader() {
        var book = dbBooks.get(0);
        var expectedItem = new BookCreateDto();
        expectedItem.setTitle(book.getTitle());
        expectedItem.setDescription(book.getDescription());
        expectedItem.setAuthors(book.getAuthors().stream().map(Author::getId).toList());
        expectedItem.setSections(book.getSections().stream().map(Section::getId).toList());
        expectedItem.setPublicationYear(3000);
        assertThatThrownBy(() -> {
            bookService.insert(expectedItem);
        }).isInstanceOf(AuthorizationDeniedException.class);
        assertThatThrownBy(() -> {
            bookService.update(1L, expectedItem);
        }).isInstanceOf(AuthorizationDeniedException.class);
        assertThatThrownBy(() -> {
            bookService.deleteById(1L);
        }).isInstanceOf(AuthorizationDeniedException.class);
    }
}
