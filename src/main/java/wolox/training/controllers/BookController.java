package wolox.training.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wolox.training.exceptions.responses.BookIdMismatchException;
import wolox.training.exceptions.responses.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@Controller
@RequestMapping("/api/training/books")
public class BookController {

    private static final String MSG = "Book not found";
    @Autowired
    private BookRepository bookRepository;

    /**
     * This method is used to greet someone by the name of by default world.
     *
     * @param name: name of the person to greet (String).
     * @param model: model to supply attributes used for rendering views.
     * @return a String greeting with the name passed in uri or world by default.
     */
    @GetMapping("greeting")
    public String greeting(
            @RequestParam(name="name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     * This method is used to get a list with all books.
     *
     * @return ResponseEntity with a list of {@link Book} with all the books,
     *         In case that the list is empty, return a ResponseEntity noContent.
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> response = bookRepository.findAll();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * This method is used to get a {@link Book} by the id.
     *
     * @param id: Id of the book to be searched (Long).
     * @return ResponseEntity with found {@link Book} with the id passed.
     * @exception BookNotFoundException: throw a {@link BookNotFoundException} in case that a book was not found by the id passed.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        Optional<Book> response = bookRepository.findById(id);
        if(response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            throw new BookNotFoundException(MSG);
        }
    }

    /**
     * This method is used to get a {@link Book} by the author.
     *
     * @param author: Author of the book to be searched (String).
     * @return ResponseEntity with found {@link Book} with the author passed.
     * @exception BookNotFoundException: throw a {@link BookNotFoundException} in case that a book was not found by the author passed.
     */
    @GetMapping("/byAuthor")
    public ResponseEntity<Book> getBookByAuthor(@RequestParam("author") String author) {
        Optional<Book> response = bookRepository.findBookByAuthor(author);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            throw new BookNotFoundException(MSG);
        }
    }

    /**
     * This method is used to create a {@link Book} with a request body Book.
     *
     * @param bookToSave: Book model class with the following attributes: (Book)
     *     genre: Genre of the book (String),
     *     author: Author of the book (String),
     *     image: Image of the book (String),
     *     title: Title of the book (String),
     *     subtitle: Subtitle of the book (String),
     *     publisher: Publisher of the book (String),
     *     year: Year of the book (String),
     *     pages: Pages of the book (Integer),
     *     isbn: Isbn of the book (String).
     * @return ResponseEntity with created {@link Book} with the attributes passed in Book model.
     */
    @PostMapping
    public ResponseEntity<Book> addBook (@RequestBody Book bookToSave) {
        Book response = bookRepository.save(bookToSave);
        return ResponseEntity.ok(response);
    }

    /**
     *This method is used to update a {@link Book} by id and a request body Book.
     *
     * @param id: Id of the book who is going to be updated (Long).
     * @param bookToUpdate: Book model class with the following attributes: (Book)
     *           id: Id of the book (Long),
     *           genre: Genre of the book (String),
     *           author: Author of the book (String),
     *           image: Image of the book (String),
     *           title: Title of the book (String),
     *           subtitle: Subtitle of the book (String),
     *           publisher: Publisher of the book (String),
     *           year: Year of the book (String),
     *           pages: Pages of the book (Integer),
     *           isbn: Isbn of the book (String).
     * @return ResponseEntity with updated {@link Book} with the attributes passed in Book model.
     * @exception BookIdMismatchException: throw a {@link BookIdMismatchException} in case that the id passed and id of the book made mismatched.
     * @exception BookNotFoundException: throw a {@link BookNotFoundException} in case that a book was not found by the id passed.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id,
            @RequestBody Book bookToUpdate) {
        if (bookToUpdate.getId()!=id) {
            throw new BookIdMismatchException("Book Id mismatched");
        }
        Optional<Book> isBookCreated = bookRepository.findById(id);
        if (isBookCreated.isPresent()) {
            Book response = bookRepository.save(bookToUpdate);
            return ResponseEntity.ok(response);
        } else {
            throw new BookNotFoundException(MSG);
        }
    }

    /**
     *This method is used to delete a {@link Book} by the id.
     *
     * @param id: Id of the book who is going to be deleted (Long).
     * @return ResponseEntity with a String message who inform that the book was successfully deleted.
     * @exception BookNotFoundException: throw a {@link BookNotFoundException} in case that a book was not found by the id passed.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById (@PathVariable("id") Long id) {
        Optional<Book> isBookCreated = bookRepository.findById(id);
        if (isBookCreated.isPresent()) {
            bookRepository.deleteById(id);
            String response = "Book Successfully Deleted";
            return ResponseEntity.ok(response);
        } else {
            throw new BookNotFoundException(MSG);
        }
    }
}
