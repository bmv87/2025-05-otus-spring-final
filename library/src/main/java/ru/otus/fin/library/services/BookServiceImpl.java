package ru.otus.fin.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.otus.fin.library.controllers.BookFilterParams;
import ru.otus.fin.library.dto.books.BookAdminListItemDto;
import ru.otus.fin.library.dto.books.BookCreateDto;
import ru.otus.fin.library.dto.books.BookDto;
import ru.otus.fin.library.dto.common.Paginated;
import ru.otus.fin.library.entities.Book;
import ru.otus.fin.library.entities.UserEntity;
import ru.otus.fin.library.exceptions.BusinessValidationException;
import ru.otus.fin.library.exceptions.EntityNotFoundException;
import ru.otus.fin.library.mappers.BookMapper;
import ru.otus.fin.library.repositories.AuthorRepository;
import ru.otus.fin.library.repositories.BookRepository;
import ru.otus.fin.library.repositories.SectionRepository;
import ru.otus.fin.library.repositories.UserRepository;
import ru.otus.fin.library.repositories.specifications.BookSpecifications;
import ru.otus.fin.library.repositories.specifications.SpecificationsHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final SectionRepository sectionRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final LocalizedMessagesService localizedMessagesService;

    @Override
    @Transactional
    public BookDto findById(long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        localizedMessagesService.getMessage("errors.book_not_found", id)));
        //https://struchkov.dev/blog/ru/hibernate-multiple-bag-fetch-exception/?ysclid=mi28zj7ccc96601111
        return bookMapper.mapToDto(book);
    }

    @Override
    @Transactional
    public Paginated<BookDto> getList(BookFilterParams filters, Pageable pageable) {
        var spec = getBookListSpecification(filters);
        var listPage = bookRepository.findAll(spec, pageable);
        var books = bookMapper.mapToListDto(listPage.getContent());
        return new Paginated<>(books, listPage.getTotalElements());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Paginated<BookAdminListItemDto> getAdminList(BookFilterParams filters, Pageable pageable) {
        var spec = getBookListSpecification(filters);
        var listPage = bookRepository.findAll(spec, pageable);
        var books = bookMapper.mapToAdminListDto(listPage.getContent());
        return new Paginated<>(books, listPage.getTotalElements());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookDto insert(BookCreateDto bookDto) {
        validate(bookDto);
        var book = bookMapper.mapToEntity(bookDto);
        fillLinkedEntities(book, bookDto);
        book = bookRepository.save(book);
        return bookMapper.mapToDto(book);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookDto update(long id, BookCreateDto bookDto) {
        var book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                localizedMessagesService.getMessage("errors.book_not_found", id)));
        validate(bookDto);
        bookMapper.mapToEntity(bookDto, book);
        fillLinkedEntities(book, bookDto);
        book = bookRepository.save(book);
        return bookMapper.mapToDto(book);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private void fillLinkedEntities(Book book, BookCreateDto bookDto) {
        var authors = authorRepository.findAllById(bookDto.getAuthors());
        if (isEmpty(authors) || bookDto.getSections().size() != authors.size()) {
            throw new EntityNotFoundException(
                    localizedMessagesService.getMessage("errors.some_authors_not_found",
                            bookDto.getSections()));
        }
        var sections = sectionRepository.findAllById(bookDto.getSections());
        if (isEmpty(sections) || bookDto.getSections().size() != sections.size()) {
            throw new EntityNotFoundException(
                    localizedMessagesService.getMessage("errors.some_sections_not_found",
                            bookDto.getSections()));
        }
        book.setAuthors(authors);
        book.setSections(sections);
        if (book.getCreatedBy() == null) {
            var user = tryGetCurrentUserEntity();
            book.setCreatedBy(user);
        }
    }

    private void validate(BookCreateDto bookDto) {
        if (bookDto.getPublicationYear() > LocalDate.now().getYear()) {
            throw new BusinessValidationException("errors.wrong_year_value");
        }
    }

    private Specification<Book> getBookListSpecification(BookFilterParams filters) {
        List<Specification<Book>> specList = new ArrayList<>();
        if (StringUtils.hasText(filters.getTitle())) {
            specList.add(BookSpecifications.hasTitle(filters.getTitle()));
        }
        if (StringUtils.hasText(filters.getDescription())) {
            specList.add(BookSpecifications.hasDescription(filters.getDescription()));
        }
        addYearSpecification(filters, specList);
        if (filters.getAuthor() != null) {
            specList.add(BookSpecifications.byAuthor(filters.getAuthor()));
        }
        if (filters.getSection() != null) {
            specList.add(BookSpecifications.bySection(filters.getSection()));
        }
        return SpecificationsHelper.and(specList);
    }

    private void addYearSpecification(BookFilterParams filters, List<Specification<Book>> specList) {
        if (filters.getYearStart() != null && filters.getYearEnd() != null) {
            specList.add(BookSpecifications.publicationYearBetween(
                    filters.getYearStart(), filters.getYearEnd()));
        } else if (filters.getYearStart() != null) {
            specList.add(BookSpecifications.publicationYearGreaterThanOrEqualTo(filters.getYearStart()));
        } else if (filters.getYearEnd() != null) {
            specList.add(BookSpecifications.publicationYearLessThanOrEqualTo(filters.getYearEnd()));
        }
    }

    private UserEntity tryGetCurrentUserEntity() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        localizedMessagesService.getMessage("errors.user_not_found",
                                auth.getName())));

        return user;
    }
}
