package ru.otus.fin.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.fin.library.dto.sections.SectionAdminListItemDto;
import ru.otus.fin.library.dto.sections.SectionDictionaryDto;
import ru.otus.fin.library.entities.Section;
import ru.otus.fin.library.mappers.SectionMapper;
import ru.otus.fin.library.repositories.SectionRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    private final SectionMapper sectionMapper;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SectionAdminListItemDto> getAdminList(String name) {
        List<Section> authors = findAllOrByName(name);
        return sectionMapper.mapToAdminListDto(authors);
    }

    @Override
    public List<SectionDictionaryDto> getDictionaryList(String name) {
        List<Section> authors = findAllOrByName(name);
        return sectionMapper.mapToDictionaryListDto(authors);
    }

    private List<Section> findAllOrByName(String name) {
        if (StringUtils.hasText(name)) {
            return sectionRepository.findByNameContainsIgnoreCase(name);
        }
        return sectionRepository.findAll();
    }
}
