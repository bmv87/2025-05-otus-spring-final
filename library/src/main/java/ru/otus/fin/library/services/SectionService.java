package ru.otus.fin.library.services;

import ru.otus.fin.library.dto.sections.SectionAdminListItemDto;
import ru.otus.fin.library.dto.sections.SectionDictionaryDto;

import java.util.List;

public interface SectionService {

    List<SectionAdminListItemDto> getAdminList(String name);

    List<SectionDictionaryDto> getDictionaryList(String name);
}
