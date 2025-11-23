package ru.otus.fin.library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.fin.library.LibraryApplication;
import ru.otus.fin.library.config.AppConfig;
import ru.otus.fin.library.config.DefaultProps;
import ru.otus.fin.library.config.SecurityConfig;
import ru.otus.fin.library.controllers.admin.AdminBookController;
import ru.otus.fin.library.dto.books.BookCreateDto;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.mappers.AuthorMapper;
import ru.otus.fin.library.mappers.BookMapper;
import ru.otus.fin.library.mappers.SectionMapper;
import ru.otus.fin.library.services.BookService;
import ru.otus.fin.library.services.LocalizedMessagesServiceImpl;
import ru.otus.fin.library.services.UserAuthorityService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.fin.library.security.AuthorityConstants.ADMIN;
import static ru.otus.fin.library.security.AuthorityConstants.READER;

@WebMvcTest(AdminBookController.class)
@Import({
        BookMapper.class, AuthorMapper.class, SectionMapper.class,
        DefaultProps.class, LocalizedMessagesServiceImpl.class})
@ContextConfiguration(classes = {
        SecurityConfig.class,
        AppConfig.class,
        LibraryApplication.class
})
public class AdminBookControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private UserAuthorityService userAuthorityService;

    private final String routePath = "/api/v1/admin/books";

    private String concatRoute(String path) {
        return routePath + path;
    }

    @DisplayName("Читатель получает 403 ошибку при попытке создать книгу")
    @Test
    @WithMockUser(username = "reader", roles = {READER})
    void postForReaderFailWith403() throws Exception {
        mvc.perform(post(routePath))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Анонимный пользователь получает 401 ошибку при попытке создать книгу")
    @Test
    void postForAnonymousFailWith401() throws Exception {
        mvc.perform(post(routePath))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Читатель получает 403 ошибку при попытке отредактировать книгу")
    @Test
    @WithMockUser(username = "reader", roles = {READER})
    void putForReaderFailWith403() throws Exception {
        mvc.perform(post(concatRoute("/{bookId}"), "1"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Анонимный пользователь получает 401 ошибку при попытке отредактировать книгу")
    @Test
    void putForAnonymousFailWith401() throws Exception {
        mvc.perform(post(concatRoute("/{bookId}"), "1"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Читатель получает 403 ошибку при попытке удалить книгу")
    @Test
    @WithMockUser(username = "reader", roles = {READER})
    void deleteForReaderFailWith403() throws Exception {
        mvc.perform(delete(concatRoute("/{bookId}"), "1"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Анонимный пользователь получает 401 ошибку при попытке удалить книгу")
    @Test
    void deleteForAnonymousFailWith401() throws Exception {
        mvc.perform(delete(concatRoute("/{bookId}"), "1"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Администратор может удалять книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void deleteForAdminSuccessReturn200() throws Exception {
        mvc.perform(delete(concatRoute("/{bookId}"), "1"))
                .andExpect(status().isOk());
    }

    @DisplayName("Администратор может создавать книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void createForAdminSuccessReturn200() throws Exception {
        var bookEditDto = new BookCreateDto(
                "title_test",
                "description_test",
                2000,
                List.of(1L),
                List.of(1L));
        when(bookService.insert(any(BookCreateDto.class)))
                .thenReturn(any(BookDto.class));

        mvc.perform(post(routePath)
                        .content(mapper.writeValueAsString(bookEditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Администратор может редактировать книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void updateForAdminSuccessReturn200() throws Exception {
        var bookEditDto = new BookCreateDto(
                "title_test",
                "description_test",
                2000,
                List.of(1L),
                List.of(1L));
        when(bookService.update(anyLong(), any(BookCreateDto.class)))
                .thenReturn(new BookDto());

        mvc.perform(put(concatRoute("/{bookId}"), "1")
                        .content(mapper.writeValueAsString(bookEditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Если DTO книги неправильно заполнено для редактирования, то вернуть 400")
    @WithMockUser(username = "admin", roles = {ADMIN})
    @ParameterizedTest()
    @MethodSource("getWrongFilledBookDtos")
    void updateWithInvalidDtoReturn400(BookCreateDto bookEditDto) throws Exception {

        mvc.perform(put(concatRoute("/{bookId}"), "1")
                        .content(mapper.writeValueAsString(bookEditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Если DTO книги неправильно заполнено для создания, то вернуть 400")
    @WithMockUser(username = "admin", roles = {ADMIN})
    @ParameterizedTest()
    @MethodSource("getWrongFilledBookDtos")
    void createWithInvalidDtoReturn400(BookCreateDto bookEditDto) throws Exception {

        mvc.perform(post(routePath)
                        .content(mapper.writeValueAsString(bookEditDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private static List<BookCreateDto> getWrongFilledBookDtos() {
        var arrWithNull = new ArrayList<Long>();
        var arrEmpty = new ArrayList<Long>();
        arrWithNull.add(null);
        return List.of(
                new BookCreateDto(null, "description_test", 2000,
                        List.of(1L), List.of(1L)),
                new BookCreateDto("", "description_test", 2000,
                        List.of(1L), List.of(1L)),
                new BookCreateDto("title_test", null, 2000,
                        List.of(1L), List.of(1L)),
                new BookCreateDto("title_test", "", 2000,
                        List.of(1L), List.of(1L)),
                new BookCreateDto("title_test", "description_test", null,
                        List.of(1L), List.of(1L)),
                new BookCreateDto("title_test", "description_test", 2000,
                        arrEmpty, List.of(1L)),
                new BookCreateDto("title_test", "description_test", 2000,
                        null, List.of(1L)),
                new BookCreateDto("title_test", "description_test", 2000,
                        arrWithNull, List.of(1L)),
                new BookCreateDto("title_test", "description_test", 2000,
                        List.of(1L), arrEmpty),
                new BookCreateDto("title_test", "description_test", 2000,
                        List.of(1L), null),
                new BookCreateDto("title_test", "description_test", 2000,
                        List.of(1L), arrWithNull)
        );
    }
}
