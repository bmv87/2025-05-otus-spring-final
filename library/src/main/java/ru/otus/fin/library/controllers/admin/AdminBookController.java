package ru.otus.fin.library.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.controllers.BookFilterParams;
import ru.otus.fin.library.dto.ErrorDto;
import ru.otus.fin.library.dto.books.BookAdminListItemDto;
import ru.otus.fin.library.dto.books.BookCreateDto;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.dto.common.Paginated;
import ru.otus.fin.library.services.BookService;

@RestController
@RequestMapping("/api/v1/admin/books")
@SecurityRequirement(name = "JWT")
@Tag(name = "Books (Admin)", description = "Methods for working with books for admin")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;

    @GetMapping()
    @Operation(summary = "Get extended book info list")
    public Paginated<BookAdminListItemDto> getList(
            @ModelAttribute BookFilterParams filters,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 20,
                    direction = Sort.Direction.DESC,
                    sort = BookAdminListItemDto.Fields.title) Pageable pageable) {

        return bookService.getAdminList(filters, pageable);
    }


    @PostMapping
    @Operation(summary = "Create book",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(
                            description = "Validation error", responseCode = "400",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class))
                    ),
                    @ApiResponse(
                            description = "Service error", responseCode = "500",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class))
                    )
            })
    public BookDto create(
            @Parameter(description = "Book create info", required = true,
                    schema = @Schema(implementation = BookCreateDto.class))
            @Valid @RequestBody BookCreateDto bookCreateDTO) {

        return bookService.insert(bookCreateDTO);
    }


    @PutMapping("/{bookId}")
    @Operation(summary = "Update book",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(
                            description = "Validation error", responseCode = "400",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class))
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
    public BookDto update(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId,
            @Parameter(description = "Book update info", required = true,
                    schema = @Schema(implementation = BookCreateDto.class))
            @Valid @RequestBody BookCreateDto bookCreateDTO) {

        return bookService.update(bookId, bookCreateDTO);
    }


    @DeleteMapping("/{bookId}")
    @Operation(summary = "Delete book",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Validation error", responseCode = "400",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class))
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
    public void delete(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId) {

        bookService.deleteById(bookId);
    }
}
