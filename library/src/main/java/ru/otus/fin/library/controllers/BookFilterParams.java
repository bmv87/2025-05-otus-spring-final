package ru.otus.fin.library.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class BookFilterParams {

    @Parameter(description = "Book title", required = false, schema = @Schema(type = "string"))
    private String title;

    @Parameter(description = "Book description", required = false, schema = @Schema(type = "string"))
    private String description;

    @Parameter(description = "Book section", required = false)
    private Long section;

    @Parameter(description = "Book author", required = false)
    private Long author;

    @Parameter(description = "Publication year start", required = false)
    private Integer yearStart;

    @Parameter(description = "Publication year end", required = false)
    private Integer yearEnd;
}