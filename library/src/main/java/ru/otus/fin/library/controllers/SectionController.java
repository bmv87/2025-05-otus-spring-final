package ru.otus.fin.library.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.fin.library.dto.sections.SectionAdminListItemDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sections")
@SecurityRequirement(name = "JWT")
@Tag(name = "Sections", description = "Methods for working with sections for user")
@RequiredArgsConstructor
public class SectionController {

    @GetMapping()
    @Operation(summary = "Get extended section info list")
    public List<SectionAdminListItemDto> getList(
            @Parameter(description = "Section name", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = "name", required = false) String name) {
        //TODO: impl
        return new ArrayList<>();
    }
}
