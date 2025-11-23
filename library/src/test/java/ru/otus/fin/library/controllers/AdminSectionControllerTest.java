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
import ru.otus.fin.library.controllers.admin.AdminSectionController;
import ru.otus.fin.library.mappers.SectionMapper;
import ru.otus.fin.library.services.LocalizedMessagesServiceImpl;
import ru.otus.fin.library.services.SectionService;
import ru.otus.fin.library.services.UserAuthorityService;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.fin.library.security.AuthorityConstants.ADMIN;
import static ru.otus.fin.library.security.AuthorityConstants.READER;

@WebMvcTest(AdminSectionController.class)
@Import({
        SectionMapper.class, DefaultProps.class,
        LocalizedMessagesServiceImpl.class})
@ContextConfiguration(classes = {
        SecurityConfig.class,
        AppConfig.class,
        LibraryApplication.class
})
public class AdminSectionControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private SectionService sectionService;

    @MockitoBean
    private UserAuthorityService userAuthorityService;

    private final String routePath = "/api/v1/admin/sections";

    private String concatRoute(String path) {
        return routePath + path;
    }

    @DisplayName("Читатель получит 403 " +
            "при получении списка разделов из контроллера для администратора")
    @Test
    @WithMockUser(username = "any", roles = {READER})
    void shouldReturn403ForNotAdmin() throws Exception {
        mvc.perform(get(routePath))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Анонимный пользователь получит 401 " +
            "при получении списка разделов из контроллера для администратора")
    @Test
    void shouldReturn401ForAnonymousUser() throws Exception {
        mvc.perform(get(routePath))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Администратор может получить список разделов " +
            "из контроллера для администратора")
    @Test
    @WithMockUser(username = "any", roles = {ADMIN})
    void shouldReturnListWithOkForAdmin() throws Exception {
        when(sectionService.getAdminList(anyString())).thenReturn(new ArrayList<>());
        mvc.perform(get(routePath))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
