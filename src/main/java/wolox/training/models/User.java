package wolox.training.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import wolox.training.exceptions.responses.BookAlreadyOwnException;

/**
 * This class is used to have a model of an user
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Id of the user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Username of the user
     */
    @Column(nullable = false)
    private String username;

    /**
     * Name of the user
     */
    @Column(nullable = false)
    private String name;

    /**
     * Birthday of the user
     */
    @Column(nullable = false)
    private LocalDate birthdate;

    /**
     * Collection of books that have the user
     */
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinTable(name = "userBook",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "bookId")
    )
    private List<Book> library = new ArrayList<>();

    public User() {
    }

    public User(Long id, String username, String name, LocalDate birthdate,
            List<Book> library) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.birthdate = birthdate;
        this.library = library;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<Book> getLibrary() {
        return Collections.unmodifiableList(library);
    }

    public void setLibrary(List<Book> books) {
        this.library = books;
    }

    /**
     * This method is used to add a {@link Book} from the library collection of an {@link User}
     *
     * @param book: Book model class with the following attributes: (Book)
     *     id: Id of the book (Long),
     *     genre: Genre of the book (String),
     *     author: Author of the book (String),
     *     image: Image of the book (String),
     *     title: Title of the book (String),
     *     subtitle: Subtitle of the book (String),
     *     publisher: Publisher of the book (String),
     *     year: Year of the book (String),
     *     pages: Pages of the book (Integer),
     *     isbn: Isbn of the book (String).
     * @exception BookAlreadyOwnException: throw a {@link BookAlreadyOwnException} in case that the book is already in the library collection
     */
    public void addBookToUser(Book book) {
        if (book == null) {
            throw new NullPointerException();
        }
        if (validateBook(book)) {
            throw new BookAlreadyOwnException("Book is already in the user collection");
        }
        this.library.add(book);
    }

    /**
     * This method is used to remove a {@link Book} from the library collection of an {@link User}.
     *
     * @param book: Book model class with the following attributes: (Book)
     *     id: Id of the book (Long),
     *     genre: Genre of the book (String),
     *     author: Author of the book (String),
     *     image: Image of the book (String),
     *     title: Title of the book (String),
     *     subtitle: Subtitle of the book (String),
     *     publisher: Publisher of the book (String),
     *     year: Year of the book (String),
     *     pages: Pages of the book (Integer),
     *     isbn: Isbn of the book (String).
     */
    public void removeBookToUser(Book book) {
        if(this.library.contains(book)) {
            this.library.remove(book);
        }
    }

    /**
     * This method is used to remove a {@link Book}  by id from the library collection of an {@link User}.
     *
     * @param id: Id of the book who is going to be removed from the library collection of the {@link User},
     */
    public void removeBookToUserById(Long id) {
        for (Book b : this.library) {
            if (b.getId().equals(id)) {
                this.library.remove(b);
            }
        }
    }

    /**
     * This method is used to validate if a {@link Book} is in the library collection of an {@link User}.
     *
     * @param book: Book model class with the following attributes: (Book)
     *     id: Id of the book (Long),
     *     genre: Genre of the book (String),
     *     author: Author of the book (String),
     *     image: Image of the book (String),
     *     title: Title of the book (String),
     *     subtitle: Subtitle of the book (String),
     *     publisher: Publisher of the book (String),
     *     year: Year of the book (String),
     *     pages: Pages of the book (Integer),
     *     isbn: Isbn of the book (String).
     * @return Boolean true if the book is already in the library collection, if the book isn't in the library return false
     */
    private Boolean validateBook(Book book) {
        return this.library.contains(book);
    }
}
