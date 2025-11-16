package ru.otus.fin.library.mappers;

import org.springframework.stereotype.Component;
import ru.otus.fin.library.dto.links.LinkCreateDto;
import ru.otus.fin.library.dto.links.LinkDto;
import ru.otus.fin.library.entities.BookLink;
import ru.otus.fin.library.entities.BookPicture;

import java.util.List;

@Component
public class LinkMapper {

    public BookLink mapToLinkEntity(LinkCreateDto dto) {
        var link = new BookLink();
        link.setTitle(dto.getTitle());
        link.setTarget(dto.getTarget());
        link.setType(dto.getType());
        return link;
    }

    public BookPicture mapToPictureEntity(LinkCreateDto dto) {
        var link = new BookPicture();
        link.setTitle(dto.getTitle());
        link.setTarget(dto.getTarget());
        link.setType(dto.getType());
        return link;
    }

    public LinkDto mapToListItemDto(BookLink entity) {
        var link = new LinkDto();
        link.setId(entity.getId());
        link.setTitle(entity.getTitle());
        link.setTarget(entity.getTarget());
        link.setType(entity.getType());
        return link;
    }

    public LinkDto mapToListItemDto(BookPicture entity) {
        var link = new LinkDto();
        link.setId(entity.getId());
        link.setTitle(entity.getTitle());
        link.setTarget(entity.getTarget());
        link.setType(entity.getType());
        return link;
    }

    public List<LinkDto> mapToLinkListDto(List<BookLink> entities) {
        return entities.stream().map(this::mapToListItemDto).toList();
    }

    public List<LinkDto> mapToPictureListDto(List<BookPicture> entities) {
        return entities.stream().map(this::mapToListItemDto).toList();
    }
}
