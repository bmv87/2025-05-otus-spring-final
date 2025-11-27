package ru.otus.fin.library.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paginated<T> {

    private List<T> items;

    private long totalCount;
}
