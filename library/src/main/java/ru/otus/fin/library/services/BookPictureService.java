package ru.otus.fin.library.services;

import ru.otus.fin.library.dto.links.LinkCreateDto;
import ru.otus.fin.library.dto.links.LinkDto;

import java.util.List;

public interface BookPictureService {

    LinkDto insert(Long bookId, LinkCreateDto linkCreateDTO);

    void deleteFromBook(Long bookId, Long linkId);

    List<LinkDto> getBookLinkList(Long bookId);
}
