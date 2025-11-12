package ru.otus.fin.library.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.dto.ErrorDto;
import ru.otus.fin.library.dto.books.BookAdminListItemDto;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.dto.books.BookWithPicturesDto;
import ru.otus.fin.library.dto.common.Paginated;
import ru.otus.fin.library.dto.links.LinkDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "JWT")
@Tag(name = "Books (Admin)", description = "Methods for view books for users")
@RequiredArgsConstructor
public class BookController {

    @GetMapping()
    @Operation(summary = "Get extended book info list")
    public Paginated<BookDto> getList(
            @Parameter(description = "Book title", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = "title", required = false) String title,
            @Parameter(description = "Book description", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = "description", required = false) String description,
            @Parameter(description = "Book section", required = false)
            @RequestParam(name = "section", required = false) Long section,
            @Parameter(description = "Book author", required = false)
            @RequestParam(name = "author", required = false) Long author,
            @Parameter(description = "Publication year start", required = false)
            @RequestParam(name = "yearStart", required = false) Integer yearStart,
            @Parameter(description = "Publication year end", required = false)
            @RequestParam(name = "yearEnd", required = false) LocalDateTime yearEnd,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 20,
                    direction = Sort.Direction.DESC,
                    sort = BookAdminListItemDto.Fields.title) Pageable pageable) {
        //TODO: impl
        return new Paginated<BookDto>(List.of(), 0);
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "Get book",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookWithPicturesDto.class))
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
    public BookWithPicturesDto getBookById(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId) {
        //TODO: impl
        return new BookWithPicturesDto();
    }


    @GetMapping("/{bookId}/links")
    @Operation(summary = "Get links of book",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LinkDto.class))
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
    public List<LinkDto> getBookLinks(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId) {
        //TODO: impl
        return new ArrayList<>();
    }
}
