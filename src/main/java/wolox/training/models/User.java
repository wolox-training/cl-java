package wolox.training.models;

import com.google.common.base.Preconditions;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import wolox.training.exceptions.responses.BookAlreadyOwnException;

/**
 * This class is used to have a model of an user
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "username")
        })
@ApiModel(description = "Users of the OpenLibraryAPI")
public class User {

    /**
     * Id of the user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @ApiModelProperty(notes = "ID of the user", example = "7")
    private Long id;

    /**
     * Username of the user
     */
    @NotNull
    @ApiModelProperty(notes = "Username of the user", required = true, example = "thiam")
    private String username;

    /**
     * Name of the user
     */
    @NotNull
    @ApiModelProperty(notes = "Name of the user", required = true, example = "Santiago")
    private String name;

    /**
     * Birthday of the user
     */
    @NotNull
    @ApiModelProperty(notes = "Birthday of the user", required = true, example = "1995-09-25")
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
    @ApiModelProperty(notes = "Collection library of the user")
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Preconditions.checkNotNull(username, "Username must not be null");
        Preconditions.checkArgument(!username.isEmpty(), "Username must not be empty");
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Preconditions.checkNotNull(name, "Name must not be null");
        Preconditions.checkArgument(!name.isEmpty(), "Name must not be empty");
        Preconditions.checkArgument(name.matches("[a-zA-Z]*"),"Name must not have numbers or invalid characters");
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        Preconditions.checkNotNull(birthdate, "Birthday must not be null");
        LocalDate actual = LocalDate.now();
        Preconditions.checkArgument(birthdate.isBefore(actual),"Birthday must be less than actual Date");
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
        this.library.remove(getBookFromCollection(book));
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
        Book response = getBookFromCollection(book);
        return this.library.contains(response);
    }

    /**
     * This method is used to get the same reference to book by author in the collection library
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
     * @return a {@link Book} with the same reference that the book in collection library if there is in there.
     */
    private Book getBookFromCollection(Book book) {
        for (Book b: this.library) {
            if (b.getAuthor().equals(book.getAuthor())){
                return b;
            }
        }
        return book;
    }
}
