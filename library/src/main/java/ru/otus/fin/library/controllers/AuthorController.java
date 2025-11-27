package ru.otus.fin.library.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.dto.ErrorDto;
import ru.otus.fin.library.dto.authors.AuthorDictionaryDto;
import ru.otus.fin.library.dto.authors.AuthorDto;
import ru.otus.fin.library.services.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@SecurityRequirement(name = "JWT")
@Tag(name = "Authors", description = "Methods for working with authors for user")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping()
    @Operation(summary = "Get author list")
    public List<AuthorDictionaryDto> getList(
            @Parameter(description = "Author name", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = "name", required = false) String name) {

        return authorService.getDictionaryList(name);
    }

    @GetMapping("/{authorId}")
    @Operation(summary = "Get author",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorDto.class))
                    ),
                    @ApiResponse(
                            description = "Not found error", responseCode = "404",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class))
                    ),
                    @ApiResponse(
                            description = "Service error", responseCode = "500",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class))
                    )
            })
    public AuthorDto getAuthorById(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long authorId) {

        return authorService.getById(authorId);
    }

}
