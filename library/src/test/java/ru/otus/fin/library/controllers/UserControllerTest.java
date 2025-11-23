package ru.otus.fin.library.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import ru.otus.fin.library.dto.users.UserInfoDto;
import ru.otus.fin.library.services.LocalizedMessagesServiceImpl;
import ru.otus.fin.library.services.UserAuthorityService;
import ru.otus.fin.library.services.UserService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({
        DefaultProps.class,
        LocalizedMessagesServiceImpl.class})
@ContextConfiguration(classes = {
        SecurityConfig.class,
        AppConfig.class,
        LibraryApplication.class
})
public class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserAuthorityService userAuthorityService;

    private final String routePath = "/api/v1/users";

    private String concatRoute(String path) {
        return routePath + path;
    }

    @DisplayName("Любой аутентифицированный пользователь может получить информацию о себе")
    @Test
    @WithMockUser(username = "any")
    void shouldReturnOkForAnyAuthenticatedUser() throws Exception {
        when(userService.getByUsername("any")).thenReturn(new UserInfoDto("any full name"));
        mvc.perform(get(concatRoute("/current")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Анонимный пользователь получит 401")
    @Test
    void shouldReturn401ForAnonymousUser() throws Exception {
        mvc.perform(get(concatRoute("/current")))
                .andExpect(status().isUnauthorized());
    }
}
