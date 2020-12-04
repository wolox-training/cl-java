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
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@Controller
@RequestMapping("/api/training/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;


    @GetMapping("greeting")
    public String greeting(
            @RequestParam(name="name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> response = bookRepository.findAll();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Integer id) {
        Optional<Book> response = bookRepository.findById(id);
        if(response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byAuthor")
    public ResponseEntity<Book> getBookByAuthor(@RequestParam("author") String author) {
        Optional<Book> response = bookRepository.findBookByAuthor(author);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Book> addBook (@RequestBody Book bookToSave) {
        Book response = bookRepository.save(bookToSave);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Integer id,
            @RequestBody Book bookToUpdate) {
        Optional<Book> isBookCreated = bookRepository.findById(id);
        if (isBookCreated.isPresent()) {
            Book response = bookRepository.save(bookToUpdate);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById (@PathVariable("id") Integer id) {
        Optional<Book> isBookCreated = bookRepository.findById(id);
        if (isBookCreated.isPresent()) {
            bookRepository.deleteById(id);
            String response = "Successfully deleted";
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
