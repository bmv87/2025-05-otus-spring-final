package ru.otus.fin.library.dto.authors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Schema
public class AuthorAdminListItemDto {

    private long id;

    private String fullName;

    private String description;

    private String createdBy;
}
