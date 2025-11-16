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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.dto.ErrorDto;
import ru.otus.fin.library.dto.links.LinkCreateDto;
import ru.otus.fin.library.dto.links.LinkDto;
import ru.otus.fin.library.services.BookLinkService;
import ru.otus.fin.library.services.BookPictureService;

@RestController
@RequestMapping("/api/v1/admin/books/{bookId}")
@SecurityRequirement(name = "JWT")
@Tag(name = "Books links and pictures (Admin)",
        description = "Methods for working with books links and pictures for admin")
@RequiredArgsConstructor
public class AdminBookLinkController {

    private final BookLinkService bookLinkService;

    private final BookPictureService bookPictureService;

    @PostMapping("/pictures")
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

        return bookPictureService.insert(bookId, linkCreateDTO);
    }


    @DeleteMapping("/pictures/{linkId}")
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

        bookPictureService.deleteFromBook(bookId, linkId);
    }


    @PostMapping("/links")
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

        return bookLinkService.insert(bookId, linkCreateDTO);
    }


    @DeleteMapping("/links/{linkId}")
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

        bookLinkService.deleteFromBook(bookId, linkId);
    }
}
