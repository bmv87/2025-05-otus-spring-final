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

    @NotEmpty(message = "{errors.fields.required}")
    private String title;

    @NotEmpty(message = "{errors.fields.required}")
    private String description;

    @NotNull(message = "{errors.fields.required}")
    private Integer publicationYear;

    @NotEmpty(message = "{errors.fields.empty_list}")
    private List<@NotNull Long> authors;

    @NotEmpty(message = "{errors.fields.empty_list}")
    private List<@NotNull Long> sections;
}
