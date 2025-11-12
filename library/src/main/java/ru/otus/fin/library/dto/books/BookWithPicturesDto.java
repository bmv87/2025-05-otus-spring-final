package ru.otus.fin.library.dto.books;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.fin.library.dto.authors.AuthorDictionaryDto;
import ru.otus.fin.library.dto.links.LinkDto;
import ru.otus.fin.library.dto.sections.SectionDictionaryDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class BookWithPicturesDto {

    private long id;

    private String title;

    private String description;

    private int publicationYear;

    private List<AuthorDictionaryDto> authors;

    private List<SectionDictionaryDto> sections;

    private List<LinkDto> pictures;
}
