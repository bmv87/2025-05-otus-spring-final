package ru.otus.fin.library.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "books-authors-sections-entity-graph",
                attributeNodes = {
                        @NamedAttributeNode("authors"),
                        @NamedAttributeNode("sections"),
                }),
        @NamedEntityGraph(name = "books-with-pictures-authors-sections-entity-graph",
                attributeNodes = {
                        @NamedAttributeNode("authors"),
                        @NamedAttributeNode("pictures"),
                        @NamedAttributeNode("sections"),
                })
})
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = true, unique = false)
    private String description;

    @Column(name = "publication_year", nullable = false, unique = false)
    private int publicationYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false, unique = false)
    private UserEntity createdBy;

    @BatchSize(size = 20)
    @ManyToMany(targetEntity = Section.class, fetch = FetchType.LAZY)
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    private List<Author> authors;

    @BatchSize(size = 20)
    @ManyToMany(targetEntity = Section.class, fetch = FetchType.LAZY)
    @JoinTable(name = "books_sections",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "section_id", referencedColumnName = "id"))
    private List<Section> sections;

    @BatchSize(size = 20)
    @ManyToMany(targetEntity = Section.class, fetch = FetchType.LAZY)
    @JoinTable(name = "books_links",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "link_id", referencedColumnName = "id"))
    private List<Link> links;

    @BatchSize(size = 20)
    @ManyToMany(targetEntity = Section.class, fetch = FetchType.LAZY)
    @JoinTable(name = "books_pictures",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "link_id", referencedColumnName = "id"))
    private List<Link> pictures;

}
