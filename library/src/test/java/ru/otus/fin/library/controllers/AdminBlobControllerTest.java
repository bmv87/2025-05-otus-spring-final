package ru.otus.fin.library.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.fin.library.LibraryApplication;
import ru.otus.fin.library.config.AppConfig;
import ru.otus.fin.library.config.DefaultProps;
import ru.otus.fin.library.config.SecurityConfig;
import ru.otus.fin.library.controllers.admin.AdminBlobController;
import ru.otus.fin.library.services.BlobService;
import ru.otus.fin.library.services.LocalizedMessagesServiceImpl;
import ru.otus.fin.library.services.UserAuthorityService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.fin.library.security.AuthorityConstants.ADMIN;
import static ru.otus.fin.library.security.AuthorityConstants.READER;

@WebMvcTest(AdminBlobController.class)
@Import({
        DefaultProps.class,
        LocalizedMessagesServiceImpl.class})
@ContextConfiguration(classes = {
        SecurityConfig.class,
        AppConfig.class,
        LibraryApplication.class
})
public class AdminBlobControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BlobService blobService;

    @MockitoBean
    private UserAuthorityService userAuthorityService;

    @MockitoBean
    private Resource resourceFile;

    private final String routePath = "/api/v1/admin/blobs";

    private String concatRoute(String path) {
        return routePath + path;
    }

    @DisplayName("Администратор может удалять книги")
    @Test
    @WithMockUser(username = "admin", roles = {ADMIN})
    void uploadForAdminSuccessReturn200() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile(
                "file",
                "filename.txt",
                "text/plain",
                "some xml".getBytes());
        when(blobService.upload(any(MultipartFile.class))).thenReturn("filename.txt");
        mvc.perform(multipart(routePath).file(firstFile).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @DisplayName("Читатель не может загружать файлы")
    @Test
    @WithMockUser(username = "any", roles = {READER})
    void uploadForNotAdminReturn403() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile(
                "file",
                "filename.txt",
                "text/plain",
                "some xml".getBytes());
        when(blobService.upload(any(MultipartFile.class))).thenReturn("filename.txt");
        mvc.perform(multipart(routePath).file(firstFile).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Анонимный пользователь не может загружать файлы")
    @Test
    void uploadForAnonymousReturn401() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile(
                "file",
                "filename.txt",
                "text/plain",
                "some xml".getBytes());
        when(blobService.upload(any(MultipartFile.class))).thenReturn("filename.txt");
        mvc.perform(multipart(routePath).file(firstFile).contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized());
    }
}
