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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.dto.ErrorDto;
import ru.otus.fin.library.dto.books.BookAdminListItemDto;
import ru.otus.fin.library.dto.books.BookCreateDto;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.dto.books.BookWithPicturesDto;
import ru.otus.fin.library.dto.common.Paginated;
import ru.otus.fin.library.dto.links.LinkCreateDto;
import ru.otus.fin.library.dto.links.LinkDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/books")
@SecurityRequirement(name = "JWT")
@Tag(name = "Books (Admin)", description = "Methods for working with books for admin")
@RequiredArgsConstructor
public class AdminBookController {

    @GetMapping()
    @Operation(summary = "Get extended book info list")
    public Paginated<BookAdminListItemDto> getList(
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
        return new Paginated<BookAdminListItemDto>(List.of(), 0);
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
        //TODO: impl
        return new BookDto();
    }


    @PutMapping("/{bookId}")
    @Operation(summary = "Update book",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookWithPicturesDto.class))
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
    public BookWithPicturesDto update(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId,
            @Parameter(description = "Book update info", required = true,
                    schema = @Schema(implementation = BookCreateDto.class))
            @Valid @RequestBody BookCreateDto bookCreateDTO) {
        //TODO: impl
        return new BookWithPicturesDto();
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
        //TODO: impl
    }


    @PostMapping("/{bookId}/pictures")
    @Operation(summary = "Add picture link to book",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LinkDto.class))
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
    public LinkDto createPictureLink(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId,
            @Parameter(description = "Link create info", required = true,
                    schema = @Schema(implementation = LinkCreateDto.class))
            @Valid @RequestBody LinkCreateDto linkCreateDTO) {
        //TODO: impl
        return new LinkDto();
    }


    @DeleteMapping("/{bookId}/pictures/{linkId}")
    @Operation(summary = "Delete picture link from book",
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
    public void deletePictureLink(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId,
            @Parameter(description = "Link id", required = true)
            @PathVariable Long linkId) {
        //TODO: impl
    }


    @PostMapping("/{bookId}/links")
    @Operation(summary = "Add links to book",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LinkDto.class))
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
    public LinkDto createLink(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId,
            @Parameter(description = "Link create info", required = true,
                    schema = @Schema(implementation = LinkCreateDto.class))
            @Valid @RequestBody LinkCreateDto linkCreateDTO) {
        //TODO: impl
        return new LinkDto();
    }


    @DeleteMapping("/{bookId}/links/{linkId}")
    @Operation(summary = "Delete link from book",
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
    public void deleteLink(
            @Parameter(description = "Book id", required = true)
            @PathVariable Long bookId,
            @Parameter(description = "Link id", required = true)
            @PathVariable Long linkId) {
        //TODO: impl
    }
}
