package ru.otus.fin.library.dto.links;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class LinkCreateDto {

    @NotEmpty
    private String title;

    @NotEmpty
    private String target;
}
