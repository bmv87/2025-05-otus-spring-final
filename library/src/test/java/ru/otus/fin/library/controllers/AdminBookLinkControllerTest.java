package ru.otus.fin.library.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import ru.otus.fin.library.controllers.admin.AdminBookLinkController;
import ru.otus.fin.library.dto.links.LinkCreateDto;
import ru.otus.fin.library.dto.links.LinkDto;
import ru.otus.fin.library.mappers.LinkMapper;
import ru.otus.fin.library.services.BookLinkService;
import ru.otus.fin.library.services.BookPictureService;
import ru.otus.fin.library.services.LocalizedMessagesServiceImpl;
import ru.otus.fin.library.services.UserAuthorityService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.fin.library.security.AuthorityConstants.ADMIN;
import static ru.otus.fin.library.security.AuthorityConstants.READER;

@WebMvcTest(AdminBookLinkController.class)
@Import({
        LinkMapper.class, DefaultProps.class,
        LocalizedMessagesServiceImpl.class})
@ContextConfiguration(classes = {
        SecurityConfig.class,
        AppConfig.class,
        LibraryApplication.class
})
public class AdminBookLinkControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookLinkService bookLinkService;

    @MockitoBean
    private BookPictureService bookPictureService;

    @MockitoBean
    private UserAuthorityService userAuthorityService;

    private final String routePath = "/api/v1/admin/books/{bookId}";

    private String concatRoute(String path) {
        return routePath + path;
    }

    private LinkCreateDto linkDto;

    private static List<LinkCreateDto> getWrongFilledLinkDtos() {
        return List.of(
                new LinkCreateDto(null, "target_test.txt", "text/plain"),
                new LinkCreateDto("", "target_test.txt", "text/plain"),
                new LinkCreateDto("title_test.txt", null, "text/plain"),
                new LinkCreateDto("title_test.txt", "", "text/plain"),
                new LinkCreateDto("title_test.txt", "target_test.txt", null),
                new LinkCreateDto("title_test.txt", "target_test.txt", ""));
    }

    @BeforeEach
    void setUp() {
        linkDto = new LinkCreateDto(
                "title_test.txt",
                "target_test.txt",
                "text/plain"
        );
        when(bookLinkService.insert(anyLong(), any(LinkCreateDto.class)))
                .thenReturn(new LinkDto());
    }

    @DisplayName("Если DTO ссылки на файл книги неправильно заполнено для создания, то вернуть 400")
    @WithMockUser(username = "admin", roles = {ADMIN})
    @ParameterizedTest()
    @MethodSource("getWrongFilledLinkDtos")
    void createBookLinkWithInvalidDtoReturn400(LinkCreateDto linkDto) throws Exception {
        mvc.perform(post(concatRoute("/links"), "1")
                        .content(mapper.writeValueAsString(linkDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Если DTO ссылки на картинкуы книги неправильно заполнено для создания, то вернуть 400")
    @WithMockUser(username = "admin", roles = {ADMIN})
    @ParameterizedTest()
    @MethodSource("getWrongFilledLinkDtos")
    void createBookPicturesWithInvalidDtoReturn400(LinkCreateDto linkDto) throws Exception {
        mvc.perform(post(concatRoute("/pictures"), "1")
                        .content(mapper.writeValueAsString(linkDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Администратор может удалять ссылку на файл книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void deleteBookLinkForAdminSuccessReturn200() throws Exception {
        mvc.perform(delete(concatRoute("/links/{linkId}"), 1, 1))
                .andExpect(status().isOk());
    }

    @DisplayName("Администратор может создавать ссылку на файл книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void createBookLinkForAdminSuccessReturn200() throws Exception {
        mvc.perform(post(concatRoute("/links"), "1")
                        .content(mapper.writeValueAsString(linkDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Администратор может удалять ссылку на картинку книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void deleteBookPictureForAdminSuccessReturn200() throws Exception {
        mvc.perform(delete(concatRoute("/pictures/{linkId}"), 1, 1))
                .andExpect(status().isOk());
    }

    @DisplayName("Администратор может создавать ссылку на картинку книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void createBookPicturekForAdminSuccessReturn200() throws Exception {
        mvc.perform(post(concatRoute("/pictures"), "1")
                        .content(mapper.writeValueAsString(linkDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Читатель не может удалять ссылку на файл книги")
    @Test
    @WithMockUser(username = "any", roles = {READER})
    void deleteBookLinkForReaderReturn403() throws Exception {
        mvc.perform(delete(concatRoute("/links/{linkId}"), 1, 1))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Читатель не  может создавать ссылку на файл книги")
    @Test
    @WithMockUser(username = "any", roles = {READER})
    void createBookLinkForReaderReturn403() throws Exception {

        mvc.perform(post(concatRoute("/links"), "1")
                        .content(mapper.writeValueAsString(linkDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Читатель не может удалять ссылку на картинку книги")
    @Test
    @WithMockUser(username = "any", roles = {READER})
    void deleteBookPictureForReaderReturn403() throws Exception {
        mvc.perform(delete(concatRoute("/pictures/{linkId}"), 1, 1))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Читатель не может создавать ссылку на картинку книги")
    @Test
    @WithMockUser(username = "any", roles = {READER})
    void createBookPictureForReaderReturn403() throws Exception {
        mvc.perform(post(concatRoute("/pictures"), "1")
                        .content(mapper.writeValueAsString(linkDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Анонимный пользователь не может удалять ссылку на файл книги")
    @Test
    void deleteBookLinkForAnonymousReturn401() throws Exception {
        mvc.perform(delete(concatRoute("/links/{linkId}"), 1, 1))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Анонимный пользователь не  может создавать ссылку на файл книги")
    @Test
    void createBookLinkForAnonymousReturn401() throws Exception {

        mvc.perform(post(concatRoute("/links"), "1")
                        .content(mapper.writeValueAsString(linkDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Анонимный пользователь не может удалять ссылку на картинку книги")
    @Test
    void deleteBookPictureForAnonymousReturn401() throws Exception {
        mvc.perform(delete(concatRoute("/pictures/{linkId}"), 1, 1))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Анонимный пользователь не может создавать ссылку на картинку книги")
    @Test
    void createBookPictureForAnonymousReturn401() throws Exception {
        mvc.perform(post(concatRoute("/pictures"), "1")
                        .content(mapper.writeValueAsString(linkDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
