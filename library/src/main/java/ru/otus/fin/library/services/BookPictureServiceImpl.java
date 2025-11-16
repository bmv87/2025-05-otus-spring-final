package ru.otus.fin.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.fin.library.dto.links.LinkCreateDto;
import ru.otus.fin.library.dto.links.LinkDto;
import ru.otus.fin.library.entities.UserEntity;
import ru.otus.fin.library.exceptions.EntityNotFoundException;
import ru.otus.fin.library.mappers.LinkMapper;
import ru.otus.fin.library.repositories.BookPictureRepository;
import ru.otus.fin.library.repositories.BookRepository;
import ru.otus.fin.library.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookPictureServiceImpl implements BookPictureService {

    private final LinkMapper linkMapper;

    private final BookRepository bookRepository;

    private final BookPictureRepository bookPictureRepository;

    private final UserRepository userRepository;

    private final LocalizedMessagesService localizedMessagesService;

    @Transactional
    @Override
    public LinkDto insert(Long bookId, LinkCreateDto linkCreateDTO) {
        try {
            var link = linkMapper.mapToPictureEntity(linkCreateDTO);
            var book = bookRepository.getReferenceById(bookId);
            var user = tryGetCurrentUserEntity();

            link.setCreatedBy(user);
            link.setBook(book);

            link = bookPictureRepository.save(link);
            return linkMapper.mapToListItemDto(link);
        } catch (jakarta.persistence.EntityNotFoundException ex) {
            throw new EntityNotFoundException(
                    localizedMessagesService.getMessage("errors.book_not_found", bookId));
        }
    }

    @Override
    public void deleteFromBook(Long bookId, Long linkId) {
        if (!bookPictureRepository.existsByIdAndBookId(linkId, bookId)) {
            throw new EntityNotFoundException(
                    localizedMessagesService.getMessage("errors.link_not_found", bookId));
        }
        bookPictureRepository.deleteById(linkId);
    }

    @Override
    public List<LinkDto> getBookLinkList(Long bookId) {
        var links = bookPictureRepository.findAllByBookId(bookId);
        return linkMapper.mapToPictureListDto(links);
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
