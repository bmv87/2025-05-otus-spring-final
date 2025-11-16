package ru.otus.fin.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.fin.library.dto.links.LinkCreateDto;
import ru.otus.fin.library.dto.links.LinkDto;
import ru.otus.fin.library.entities.UserEntity;
import ru.otus.fin.library.exceptions.EntityNotFoundException;
import ru.otus.fin.library.mappers.LinkMapper;
import ru.otus.fin.library.repositories.BookLinkRepository;
import ru.otus.fin.library.repositories.BookRepository;
import ru.otus.fin.library.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookLinkServiceImpl implements BookLinkService {

    private final LinkMapper linkMapper;

    private final BookRepository bookRepository;

    private final BookLinkRepository bookLinkRepository;

    private final UserRepository userRepository;

    private final LocalizedMessagesService localizedMessagesService;

    @Override
    public LinkDto insert(Long bookId, LinkCreateDto linkCreateDTO) {
        try {
            var link = linkMapper.mapToLinkEntity(linkCreateDTO);
            var book = bookRepository.getReferenceById(bookId);
            var user = tryGetCurrentUserEntity();

            link.setCreatedBy(user);
            link.setBook(book);

            link = bookLinkRepository.save(link);
            return linkMapper.mapToListItemDto(link);
        } catch (jakarta.persistence.EntityNotFoundException ex) {
            throw new EntityNotFoundException(
                    localizedMessagesService.getMessage("errors.book_not_found", bookId));
        }
    }

    @Override
    public void deleteFromBook(Long bookId, Long linkId) {
        if (!bookLinkRepository.existsByIdAndBookId(linkId, bookId)) {
            throw new EntityNotFoundException(
                    localizedMessagesService.getMessage("errors.link_not_found", bookId));
        }
        bookLinkRepository.deleteById(linkId);
    }

    @Override
    public List<LinkDto> getBookLinkList(Long bookId) {
        var links = bookLinkRepository.findAllByBookId(bookId);
        return linkMapper.mapToLinkListDto(links);
    }

    private UserEntity tryGetCurrentUserEntity() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByUsername(auth.getName());
        if (user.isEmpty()) {
            throw new EntityNotFoundException(
                    localizedMessagesService.getMessage("errors.user_not_found",
                            auth.getName()));
        }
        return user.get();
    }
}
