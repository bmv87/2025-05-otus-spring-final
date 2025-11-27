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
import ru.otus.fin.library.dto.sections.SectionDictionaryDto;
import ru.otus.fin.library.services.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sections")
@SecurityRequirement(name = "JWT")
@Tag(name = "Sections", description = "Methods for working with sections for user")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @GetMapping()
    @Operation(summary = "Get extended section info list")
    public List<SectionDictionaryDto> getList(
            @Parameter(description = "Section name", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = "name", required = false) String name) {

        return sectionService.getDictionaryList(name);
    }
}
