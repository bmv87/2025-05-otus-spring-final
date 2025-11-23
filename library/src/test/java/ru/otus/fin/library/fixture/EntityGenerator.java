package ru.otus.fin.library.fixture;

import lombok.AllArgsConstructor;
import ru.otus.fin.library.entities.Author;
import ru.otus.fin.library.entities.Book;
import ru.otus.fin.library.entities.BookLink;
import ru.otus.fin.library.entities.BookPicture;
import ru.otus.fin.library.entities.Section;
import ru.otus.fin.library.entities.UserEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class EntityGenerator {

    @AllArgsConstructor
    static class MimeInfo {
        String type;
        String extension;
    }

    static final Map<Long, MimeInfo> linkMimeTypes = Map.of(
            1L, new MimeInfo("application/pdf", ".pdf"),
            2L, new MimeInfo("application/epub+zip", ".epub"),
            3L, new MimeInfo("application/vnd.djvu", ".djvu"),
            4L, new MimeInfo("application/vnd.djvu", ".djvu")
    );

    static final Map<Long, MimeInfo> pictureMimeTypes = Map.of(
            1L, new MimeInfo("image/webp", ".webp"),
            2L, new MimeInfo("image/webp", ".webp"),
            3L, new MimeInfo("image/webp", ".webp")
    );

    public static UserEntity getUser() {
        var user = new UserEntity();
        user.setId(1L);
        user.setUsername("admin");
        user.setFirstName("Mary");
        user.setLastName("B");
        user.setMiddleName("V");
        user.setBirthday(LocalDate.parse("2012-08-31"));
        return user;
    }

    public static Author getAuthor(Long id) {
        return new Author(id, "author_" + id, "description_" + id, getUser());
    }

    public static List<Author> getAuthors() {
        return LongStream.range(1, 4).boxed()
                .map(EntityGenerator::getAuthor)
                .toList();
    }

    public static List<Author> getAuthorsByIds(List<Long> ids) {
        return ids.stream()
                .map(EntityGenerator::getAuthor)
                .toList();
    }

    public static Section getSection(Long id) {
        return new Section(id, "section_" + id, getUser());
    }

    public static List<Section> getSections() {
        return LongStream.range(1, 4).boxed()
                .map(EntityGenerator::getSection)
                .toList();
    }


    public static List<Section> getSectionsByIds(List<Long> ids) {
        return ids.stream()
                .map(EntityGenerator::getSection)
                .toList();
    }

    public static BookLink getBookLink(Long id, Long bookId) {
        var mimeInfo = linkMimeTypes.get(id);
        return new BookLink(
                id,
                mimeInfo.type,
                "book_" + bookId + mimeInfo.extension,
                "link_" + bookId + "_" + id + mimeInfo.extension,
                null,
                getUser());
    }

    public static List<BookLink> getBookLinks(Long bookId, List<Long> ids) {
        return ids.stream()
                .map(id -> getBookLink(id, bookId))
                .toList();
    }

    public static BookPicture getBookPicture(Long id, Long bookId) {
        var mimeInfo = pictureMimeTypes.get(id);
        return new BookPicture(
                id,
                mimeInfo.type,
                "book_pic_" + id + mimeInfo.extension,
                "picture_" + bookId + "_" + id + mimeInfo.extension,
                null,
                getUser());
    }

    public static List<BookPicture> getBookPictures(Long bookId, List<Long> ids) {
        return ids.stream()
                .map(id -> getBookPicture(id, bookId))
                .toList();
    }

    public static Book getBook(Long id, List<Long> authorsIds, List<Long> sectionsIds, List<Long> linksIds, List<Long> picsIds) {
        final int startYear = 2022;
        return new Book(
                id,
                "book_" + id,
                "description_" + id,
                startYear + id.intValue(),
                getUser(),
                getAuthorsByIds(authorsIds),
                getSectionsByIds(sectionsIds),
                getBookLinks(id, linksIds),
                getBookPictures(id, picsIds)

        );
    }

    public static List<Book> getBooks() {
        return List.of(
                getBook(1L, List.of(1L, 2L), List.of(1L, 2L), List.of(1L, 2L, 3L), List.of(1L, 2L)),
                getBook(2L, List.of(2L), List.of(2L), List.of(), List.of()),
                getBook(3L, List.of(3L), List.of(3L), List.of(), List.of())
        );
    }
}
