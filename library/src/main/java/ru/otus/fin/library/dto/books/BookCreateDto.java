package ru.otus.fin.library.dto.books;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class BookCreateDto {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    private Integer publicationYear;

    @NotEmpty
    private List<@NotNull Long> authors;

    @NotEmpty
    private List<@NotNull Long> sections;
}
