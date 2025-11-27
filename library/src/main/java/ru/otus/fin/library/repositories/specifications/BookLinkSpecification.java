package ru.otus.fin.library.repositories.specifications;

import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.fin.library.entities.Book;
import ru.otus.fin.library.entities.BookLink;

public class BookLinkSpecification {
    public static Specification<BookLink> byBookId(Long bookId) {
        return (root, query, criteriaBuilder) -> {
            Path<Book> booksJoin = root.get(BookLink.Fields.book);
            return criteriaBuilder.equal(booksJoin.get(Book.Fields.id), bookId);
        };
    }

    public static Specification<BookLink> byLinkId(Long linkId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(BookLink.Fields.id), linkId);
        };
    }
}
