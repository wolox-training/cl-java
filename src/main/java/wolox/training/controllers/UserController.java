package wolox.training.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.responses.UserIdMismatchException;
import wolox.training.exceptions.responses.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/training/users")
public class UserController {

    private static final String MSGNOTFOUND = "User not found";
    private static final String MSGIDMISMATCHED = "User Id mismatched";
    private static final String MSGSUCCESSFULLYDELETED = "User Successfully Deleted";

    @Autowired
    private UserRepository userRepository;

    /**
     * This method is used to get a list with all {@link User}s.
     *
     * @return ResponseEntity with a list of {@link User} with all users,
     *         In case that the list is empty, return ResponseEntity noContent.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> response = userRepository.findAll();
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * This method is used to get a {@link User} by id.
     *
     * @param id: Id of the user to be searched (Long).
     * @return ResponseEntity with found {@link User} with the id passed.
     * @exception UserNotFoundException: throw a {@link UserNotFoundException} in case that an {@link User} was not found by the id passed.
     */
    @GetMapping("{/id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> response = userRepository.findById(id);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            throw new UserNotFoundException(MSGNOTFOUND);
        }
    }

    /**
     * This method is used to get an {@link User} by username.
     *
     * @param username: Username of the user to be searched (String).
     * @return ResponseEntity with found {@link User} with the username passed.
     * @exception UserNotFoundException: throw a {@link UserNotFoundException} in case that an {@link User} was not found by the username passed
     */
    @GetMapping("/byUsername")
    public ResponseEntity<User> getUserByUsername(@RequestParam("username") String username) {
        Optional<User> response = userRepository.findByUsername(username);
        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            throw new UserNotFoundException(MSGNOTFOUND);
        }
    }

    /**
     * This method is used to create an {@link User} with a request body User.
     *
     * @param userToSave: User model class with the following attributes:
     *                  username: Username of the user (String),
     *                  name: Name of the user (String),
     *                  birthday: Birthday of the user (LocalDate),
     *                  List{@link Book}: Books of the library of the user (Can be empty) or not pass.
     * @return ResponseEntity with the {@link User} created with the attributes passed.
     */
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User userToSave) {
        User response = userRepository.save(userToSave);
        return ResponseEntity.ok(response);
    }

    /**
     * This method is used to update an {@link User} by id and request body User.
     *
     * @param id: Id of the user who is going to be updated (Long),
     * @param userToUpdate: User model class with the following attributes:
     *                 username: Username of the user (String),
     *                 name: Name of the user (String),
     *                 birthday: Birthday of the user (LocalDate),
     *                 List{@link Book}: Books of the library of the user (Can be empty) or not pass.
     * @return ResponseEntity with the {@link User} updated with the attributes passed.
     * @exception UserIdMismatchException: throw an {@link UserIdMismatchException} in case that the id passed not matched with the {@link User} passed.
     * @exception UserNotFoundException: throw an {@link UserNotFoundException} in case that the {@link User} was not found by the id passed.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id,
            @RequestBody User userToUpdate) {
        if(!userToUpdate.getId().equals(id)) {
            throw new UserIdMismatchException(MSGIDMISMATCHED);
        }
        Optional<User> isUserCreated = userRepository.findById(id);
        if (isUserCreated.isPresent()) {
            User response = userRepository.save(userToUpdate);
            return ResponseEntity.ok(response);
        } else {
            throw new UserNotFoundException(MSGNOTFOUND);
        }
    }

    /**
     * This method is used to delete an {@link User} by the id.
     *
     * @param id: Id of the {@link User} who is going to be deleted (Long).
     * @return ResponseEntity with a String message who inform that the user was successfully deleted.
     * @exception UserNotFoundException: throw an {@link UserNotFoundException} in case that the {@link User} was not found by the id passed.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        Optional<User> isUserCreated = userRepository.findById(id);
        if (isUserCreated.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(MSGSUCCESSFULLYDELETED);
        } else {
            throw new UserNotFoundException(MSGNOTFOUND);
        }
    }

    /**
     * This method is used to add a {@link Book} to the {@link User} collection library.
     *
     * @param idUser: Id of the user who is going to add the {@link Book} to the library.
     * @param bookToAdd: Book model class with the following attributes: (Book)
     *           id: Id of the book (Long),
     *           genre: Genre of the book (String),
     *           author: Author of the book (String),
     *           image: Image of the book (String),
     *          title: Title of the book (String),
     *          subtitle: Subtitle of the book (String),
     *            publisher: Publisher of the book (String),
     *          year: Year of the book (String),
     *           pages: Pages of the book (Integer),
     *          isbn: Isbn of the book (String).
     * @return ResponseEntity with the {@link User} updated with the new {@link Book} in the library (Long).
     * @exception UserNotFoundException: throw an {@link UserNotFoundException} in case that the {@link User} was not found.
     * @exception wolox.training.exceptions.responses.BookAlreadyOwnException: throw a {@link wolox.training.exceptions.responses.BookAlreadyOwnException}
     *                                                                         In case that the book is already in the {@link User} collection library.
     *
     */
    @PostMapping("/{id}/addBook")
    public ResponseEntity<User> addBookToLibraryUser(@PathVariable("id") Long idUser,
            @RequestBody Book bookToAdd) {
        Optional<User> isUserCreated = userRepository.findById(idUser);
        if (isUserCreated.isPresent()) {
            isUserCreated.get().addBookToUser(bookToAdd);
            User response = userRepository.save(isUserCreated.get());
            return ResponseEntity.ok(response);
        } else {
            throw new UserNotFoundException(MSGNOTFOUND);
        }
    }

    /**
     * This method is used to remove a {@link Book} from the {@link User} collection library.
     *
     * @param idUser: Id of the {@link User} who is going to remove the {@link Book} from the library (Long).
     * @param bookToRemove: Book model class with the following attributes: (Book)
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
     * @return ResponseEntity with the {@link User} updated with the collection library without the {@link Book} removed.
     * @exception UserNotFoundException: throw an {@link UserNotFoundException} in case that the {@link User} was not found by the id passed.
     */
    @DeleteMapping("/{id}/removeBook")
    public ResponseEntity<User> removeBookFromLibraryUser(@PathVariable("id") Long idUser,
            @RequestBody Book bookToRemove) {
        Optional<User> isUserCreated = userRepository.findById(idUser);
        if (isUserCreated.isPresent()) {
            isUserCreated.get().removeBookToUser(bookToRemove);
            User response = userRepository.save(isUserCreated.get());
            return ResponseEntity.ok(response);
        } else {
            throw new UserNotFoundException(MSGNOTFOUND);
        }
    }
}
