package ru.otus.fin.library.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.dto.authors.AuthorAdminListItemDto;
import ru.otus.fin.library.services.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/authors")
@SecurityRequirement(name = "JWT")
@Tag(name = "Authors (Admin)", description = "Methods for working with authors for admin")
@RequiredArgsConstructor
public class AdminAuthorController {

    private final AuthorService authorService;

    @GetMapping()
    @Operation(summary = "Get extended author info list")
    public List<AuthorAdminListItemDto> getList(
            @Parameter(description = "Author name", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = "name", required = false) String name) {
        return authorService.getAdminList(name);
    }
}
