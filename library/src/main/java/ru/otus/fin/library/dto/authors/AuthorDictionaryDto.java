package ru.otus.fin.library.dto.authors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class AuthorDictionaryDto {

    private long id;

    private String name;
}
