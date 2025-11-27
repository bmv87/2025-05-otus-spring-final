package ru.otus.fin.library.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.dto.ErrorDto;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.dto.users.UserInfoDto;
import ru.otus.fin.library.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "JWT")
@Tag(name = "User info", description = "Current user info")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/current")
    @Operation(summary = "Current user info",
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
    public UserInfoDto getCurrentUser(Authentication auth) {
        return userService.getByUsername(auth.getName());
    }
}
