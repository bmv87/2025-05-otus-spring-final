package ru.otus.fin.library.mappers;

import org.springframework.stereotype.Component;
import ru.otus.fin.library.dto.sections.SectionAdminListItemDto;
import ru.otus.fin.library.dto.sections.SectionDictionaryDto;
import ru.otus.fin.library.entities.Section;

import java.util.List;

@Component
public class SectionMapper {

    public SectionDictionaryDto mapToDictionaryDto(Section entity) {
        var section = new SectionDictionaryDto();
        section.setId(entity.getId());
        section.setName(entity.getName());
        return section;
    }

    public SectionAdminListItemDto mapToListItemDto(Section entity) {
        var section = new SectionAdminListItemDto();
        section.setId(entity.getId());
        section.setName(entity.getName());
        section.setCreatedBy(entity.getCreatedBy().getFullName());
        return section;
    }

    public List<SectionAdminListItemDto> mapToAdminListDto(List<Section> entities) {
        return entities.stream().map(this::mapToListItemDto).toList();
    }

    public List<SectionDictionaryDto> mapToDictionaryListDto(List<Section> entities) {
        return entities.stream().map(this::mapToDictionaryDto).toList();
    }
}
