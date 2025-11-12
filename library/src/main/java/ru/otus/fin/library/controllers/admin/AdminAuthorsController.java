package ru.otus.fin.library.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.dto.common.Paginated;
import ru.otus.fin.library.dto.authors.AuthorAdminListItemDto;

import java.util.List;

@RestController
@RequestMapping("/admin/authors")
@SecurityRequirement(name = "JWT")
@Tag(name = "Authors (Admin)", description = "Methods for working with authors for admin")
@RequiredArgsConstructor
public class AdminAuthorsController {

    @GetMapping()
    @Operation(summary = "Get extended author info list")
    public Paginated<AuthorAdminListItemDto> getList(
            @Parameter(description = "Author name", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = "name", required = false) String name,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 20,
                    direction = Sort.Direction.DESC,
                    sort = AuthorAdminListItemDto.Fields.fullName) Pageable pageable) {
        //TODO: impl
        return new Paginated<AuthorAdminListItemDto>(List.of(), 0);
    }
}
