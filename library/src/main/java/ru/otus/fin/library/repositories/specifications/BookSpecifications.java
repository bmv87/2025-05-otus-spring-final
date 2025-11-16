package ru.otus.fin.library.repositories.specifications;

import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.fin.library.entities.Book;
import ru.otus.fin.library.entities.Section;

public class BookSpecifications {

    public static Specification<Book> bySection(Long value) {
        return (root, query, criteriaBuilder) -> {
            Path<Section> sectionsJoin = root.get(Book.Fields.sections);
            return criteriaBuilder.equal(sectionsJoin.get(Section.Fields.id), value);
        };
    }

    public static Specification<Book> byAuthor(Long value) {
        return (root, query, criteriaBuilder) -> {
            Path<Section> authorsJoin = root.get(Book.Fields.authors);
            return criteriaBuilder.equal(authorsJoin.get(Section.Fields.id), value);
        };
    }

    public static Specification<Book> hasTitle(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get(Book.Fields.title)), "%" + value.toLowerCase() + "%");
    }

    public static Specification<Book> hasDescription(String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get(Book.Fields.description)), "%" + value.toLowerCase() + "%");
    }

    public static Specification<Book> publicationYearBetween(Integer startYear, Integer endYear) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(
                root.get(Book.Fields.publicationYear), startYear, endYear);
    }

    public static Specification<Book> publicationYearLessThanOrEqualTo(Integer endYear) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                root.get(Book.Fields.publicationYear), endYear);
    }

    public static Specification<Book> publicationYearGreaterThanOrEqualTo(Integer startYear) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                root.get(Book.Fields.publicationYear), startYear);
    }
}
