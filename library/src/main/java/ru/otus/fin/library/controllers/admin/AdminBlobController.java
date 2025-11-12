package ru.otus.fin.library.controllers.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.fin.library.dto.ErrorDto;
import ru.otus.fin.library.dto.UploadResultDto;

@RestController
@RequestMapping("/admin/blobs")
@SecurityRequirement(name = "JWT")
@Tag(name = "Blobs (Admin)", description = "Methods for working with blobs for admin")
@RequiredArgsConstructor
public class AdminBlobController {

    @PostMapping("")
    @Operation(summary = "Upload File",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UploadResultDto.class))
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
    public UploadResultDto uploadFile(
            @ParameterObject
            @RequestParam("file") MultipartFile file) {

        String fileName = file.getOriginalFilename();
        //TODO: impl
        return new UploadResultDto(fileName);
    }
}
