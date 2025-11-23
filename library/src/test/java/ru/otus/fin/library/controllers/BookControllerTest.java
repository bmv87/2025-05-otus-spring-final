package ru.otus.fin.library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.fin.library.LibraryApplication;
import ru.otus.fin.library.config.AppConfig;
import ru.otus.fin.library.config.DefaultProps;
import ru.otus.fin.library.config.SecurityConfig;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.dto.common.Paginated;
import ru.otus.fin.library.mappers.AuthorMapper;
import ru.otus.fin.library.mappers.BookMapper;
import ru.otus.fin.library.mappers.SectionMapper;
import ru.otus.fin.library.services.BookLinkService;
import ru.otus.fin.library.services.BookPictureService;
import ru.otus.fin.library.services.BookService;
import ru.otus.fin.library.services.LocalizedMessagesServiceImpl;
import ru.otus.fin.library.services.UserAuthorityService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import({
        BookMapper.class, AuthorMapper.class, SectionMapper.class,
        DefaultProps.class, LocalizedMessagesServiceImpl.class})
@ContextConfiguration(classes = {
        SecurityConfig.class,
        AppConfig.class,
        LibraryApplication.class
})
public class BookControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookLinkService bookLinkService;

    @MockitoBean
    private BookPictureService bookPictureService;

    @MockitoBean
    private UserAuthorityService userAuthorityService;

    private final String routePath = "/api/v1/books";

    private String concatRoute(String path) {
        return routePath + path;
    }

    @DisplayName("Любой пользователь может получить список книг")
    @Test
    void shouldReturnOkForAnyUser() throws Exception {
        when(bookService.getList(any(BookFilterParams.class), any(Pageable.class))).thenReturn(new Paginated<>());
        mvc.perform(get(routePath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Любой пользователь может получить информацию о книге")
    @Test
    void shouldReturnItemWithOkForAnyUser() throws Exception {
        when(bookService.findById(anyLong())).thenReturn(new BookDto());
        mvc.perform(get(concatRoute("/{id}"), 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Любой пользователь может получить список картинок книги")
    @Test
    void shouldReturnPicturesListWithOkForAnyUser() throws Exception {
        when(bookPictureService.getBookLinkList(anyLong())).thenReturn(new ArrayList<>());
        mvc.perform(get(concatRoute("/{bookId}/pictures"), 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Неаутентифицированный пользователь не может получить список ссылок на файлы книги")
    @Test
    void shouldReturn401ForAnonymousUser() throws Exception {
        mvc.perform(get(concatRoute("/{bookId}/links"), 1))
                .andExpect(status().isUnauthorized());
    }
}
