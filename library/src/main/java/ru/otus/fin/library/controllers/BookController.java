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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.dto.ErrorDto;
import ru.otus.fin.library.dto.books.BookAdminListItemDto;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.dto.common.Paginated;
import ru.otus.fin.library.dto.links.LinkDto;
import ru.otus.fin.library.services.BookLinkService;
import ru.otus.fin.library.services.BookPictureService;
import ru.otus.fin.library.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@SecurityRequirement(name = "JWT")
@Tag(name = "Books (Admin)", description = "Methods for view books for users")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookLinkService bookLinkService;

    private final BookPictureService bookPictureService;

    @GetMapping()
    @Operation(summary = "Get extended book info list")
    public Paginated<BookDto> getList(
            @ModelAttribute BookFilterParams filters,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 20,
                    direction = Sort.Direction.DESC,
                    sort = BookAdminListItemDto.Fields.title) Pageable pageable) {

        return bookService.getList(filters, pageable);
    }

    @GetMapping("/{bookId}")
    @Operation(summary = "Get book",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))
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
    public BookDto getBookById(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId) {

        return bookService.findById(bookId);
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

        return bookLinkService.getBookLinkList(bookId);
    }

    @GetMapping("/{bookId}/pictures")
    @Operation(summary = "Get pictures of book",
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
    public List<LinkDto> getBookPictures(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId) {

        return bookPictureService.getBookLinkList(bookId);
    }
}
