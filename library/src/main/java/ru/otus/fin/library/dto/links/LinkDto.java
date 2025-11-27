package ru.otus.fin.library.dto.links;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class LinkDto {

    private long id;

    private String title;

    private String target;

    private String type;
}
