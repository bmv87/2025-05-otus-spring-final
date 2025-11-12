package ru.otus.fin.library.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blobs")
@SecurityRequirement(name = "JWT")
@Tag(name = "Blobs", description = "Methods for working with blobs for user")
@RequiredArgsConstructor
public class BlobController {

    @GetMapping("/{filePath}")
    @Operation(summary = "Download file")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "File path", required = true)
            @PathVariable String filePath) {
        //TODO:
        return null;
    }
}
