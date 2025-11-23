package ru.otus.fin.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.fin.library.LibraryApplication;
import ru.otus.fin.library.dto.links.LinkCreateDto;
import ru.otus.fin.library.fixture.EntityGenerator;
import ru.otus.fin.library.mappers.LinkMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.otus.fin.library.security.AuthorityConstants.ADMIN;
import static ru.otus.fin.library.security.AuthorityConstants.READER;

@DisplayName("Сервис для работы с ссылками на картинки книг")
@SpringBootTest(
        classes = {
                LinkMapper.class, BookPictureServiceImpl.class, LibraryApplication.class
        }
)
@Transactional(propagation = Propagation.NEVER)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookPictureServiceTest {

    @Autowired
    private LinkMapper linkMapper;

    @Autowired
    private BookPictureServiceImpl bookPictureService;


    @DisplayName("Администратор может создавать ссылку на картинку книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void createBookPictureSuccess() {
        var link = EntityGenerator.getBookPicture(3L, 1L);
        var inputItem = new LinkCreateDto(link.getTitle(), link.getTarget(), link.getType());
        link.setId(0);

        var actualItem = bookPictureService.insert(1L, inputItem);
        assertThat(actualItem)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(inputItem);
    }

    @DisplayName("Администратор может удалить ссылку на картинку книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void deleteBookPictureSuccess() {
        var expectedList = bookPictureService.getBookLinkList(1L);
        var link = expectedList.get(0);
        expectedList = expectedList.stream().filter(l -> l.getId() != link.getId()).toList();
        bookPictureService.deleteFromBook(1L, link.getId());

        var actualList = bookPictureService.getBookLinkList(1L);
        assertThat(actualList).isNotEmpty()
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expectedList);
    }


    @DisplayName("Читатель не может создавать/удалять ссылку на картинку книги")
    @Test
    @WithMockUser(username = "reader1", roles = {READER})
    void createDeleteAuthorizationDeniedExceptionForReader() {
        var link = EntityGenerator.getBookPicture(3L, 1L);
        var inputItem = new LinkCreateDto(link.getTitle(), link.getTarget(), link.getType());
        link.setId(0);

        assertThatThrownBy(() -> {
            bookPictureService.insert(1L, inputItem);
        }).isInstanceOf(AuthorizationDeniedException.class);

        assertThatThrownBy(() -> {
            bookPictureService.deleteFromBook(1L, 1L);
        }).isInstanceOf(AuthorizationDeniedException.class);
    }

    @DisplayName("Читатель может получить список ссылок на картинки книги")
    @Test
    void getBookPicturesForAnyUsers() {
        var links = EntityGenerator.getBookPictures(1L, List.of(1L, 2L));
        var expectedList = linkMapper.mapToPictureListDto(links);

        var actualList = bookPictureService.getBookLinkList(1L);

        assertThat(actualList).isNotEmpty()
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expectedList);
    }
}
