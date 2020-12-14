package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Arrays;
import java.util.List;
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
import wolox.training.exceptions.responses.UsernameAlreadyTakenException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
@Api(value = "User microservice", tags = "This Services REST has a CRUD for Users")
public class UserController {

    private static final String USER_NOT_FOUND_MSG = "User not found";
    private static final String ID_MISMATCHED_MSG = "User Id mismatched";
    private static final String SUCCESSFULLY_DELETED_MSG = "User Successfully Deleted";
    private static final String USERNAME_TAKEN_MSG = "The username is already taken";

    @Autowired
    private UserRepository userRepository;

    /**
     * This method is used to get a {@link User} by id.
     *
     * @param id: Id of the user to be searched (Long).
     * @return ResponseEntity with found {@link User} with the id passed.
     * @exception UserNotFoundException: throw a {@link UserNotFoundException} in case that an {@link User} was not found by the id passed.
     */
    @ApiOperation(value = "Get an user by ID", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieve user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @ApiParam(value = "Id of the user that's need to be searched", required = true, example = "7")
            @PathVariable("id") Long id) {
        User response = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG));
        return ResponseEntity.ok(response);
    }

    /**
     * This method is used to get an {@link User} by username or a List with all {@link User}.
     *
     * @param username: Username of the user to be searched (String).
     * @return ResponseEntity with found {@link User} with the username passed.
     * @exception UserNotFoundException: throw a {@link UserNotFoundException} in case that an {@link User} was not found by the username passed
     */
    @ApiOperation(value = "Get an user by the username", response = User.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieve user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsersOrUserByUsername(
            @ApiParam(value = "Username of the user that's need to be searched",  example = "rider")
            @RequestParam(value = "username", required = false, defaultValue = "") String username) {
        if(username.isEmpty()){
            return ResponseEntity.ok(userRepository.findAll());
        }
        User response = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG));
        return ResponseEntity.ok(Arrays.asList(response));
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
    @ApiOperation(value = "Add an user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created user"),
            @ApiResponse(code = 409, message = "Username already taken")
    })
    @PostMapping
    public ResponseEntity<User> addUser(
            @ApiParam(value = "User object to be created", required = true)
            @RequestBody User userToSave) {
        if(userRepository.findByUsername(userToSave.getUsername()).isPresent()) {
            throw new UsernameAlreadyTakenException(USERNAME_TAKEN_MSG);
        }
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
    @ApiOperation(value = "Updated an user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user"),
            @ApiResponse(code = 400, message = "ID of user an ID passed mismatched"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 409, message = "Username already taken")
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @ApiParam(value = "ID of the user that's need to be updated", required = true, example = "7")
            @PathVariable("id") Long id,
            @ApiParam(value = "User object to be updated", required = true)
            @RequestBody User userToUpdate) {
        if(!userToUpdate.getId().equals(id)) {
            throw new UserIdMismatchException(ID_MISMATCHED_MSG);
        }
        if((userRepository.findByUsername(userToUpdate.getUsername()).isPresent())&&
                (!(userRepository.findByUsername(userToUpdate.getUsername()).get().getId().equals(id)))) {
            throw new UsernameAlreadyTakenException(USERNAME_TAKEN_MSG);
        }
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG));
        User response = userRepository.save(userToUpdate);
        return ResponseEntity.ok(response);
    }

    /**
     * This method is used to delete an {@link User} by the id.
     *
     * @param id: Id of the {@link User} who is going to be deleted (Long).
     * @return ResponseEntity with a String message who inform that the user was successfully deleted.
     * @exception UserNotFoundException: throw an {@link UserNotFoundException} in case that the {@link User} was not found by the id passed.
     */
    @ApiOperation(value = "Delete user by ID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(
            @ApiParam(value = "ID of the user that's need to be deleted", required = true, example = "6")
            @PathVariable("id") Long id) {
        User isUserCreated = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG));
        userRepository.deleteById(isUserCreated.getId());
        return ResponseEntity.ok(SUCCESSFULLY_DELETED_MSG);
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
     *           title: Title of the book (String),
     *           subtitle: Subtitle of the book (String),
     *           publisher: Publisher of the book (String),
     *           year: Year of the book (String),
     *           pages: Pages of the book (Integer),
     *           isbn: Isbn of the book (String).
     * @return ResponseEntity with the {@link User} updated with the new {@link Book} in the library (Long).
     * @exception UserNotFoundException: throw an {@link UserNotFoundException} in case that the {@link User} was not found.
     * @exception wolox.training.exceptions.responses.BookAlreadyOwnException: throw a {@link wolox.training.exceptions.responses.BookAlreadyOwnException}
     *                                                                         In case that the book is already in the {@link User} collection library.
     */
    @ApiOperation(value = "Add a book to the user library", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added book to user library"),
            @ApiResponse(code = 400, message = "Book is already owned by the user"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @PostMapping("/{id}/addBook")
    public ResponseEntity<User> addBookToLibraryUser(
            @ApiParam(value = "ID of the user where is going to be add the book to the library", required = true, example = "7")
            @PathVariable("id") Long idUser,
            @ApiParam(value = "Book object who is going to be added to the user library", required = true)
            @RequestBody Book bookToAdd) {
        User isUserCreated = userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MSG));
        isUserCreated.addBookToUser(bookToAdd);
        User response = userRepository.save(isUserCreated);
        return ResponseEntity.ok(response);
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
    @ApiOperation(value = "Remove a book from the user library", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully removed book from user library"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @DeleteMapping("/{id}/removeBook")
    public ResponseEntity<User> removeBookFromLibraryUser(
            @ApiParam(value = "ID of the user where is going to remove the book from library", required = true, example = "7")
            @PathVariable("id") Long idUser,
            @ApiParam(value = "Book object who is going to be deleted from library", required = true)
            @RequestBody Book bookToRemove) {
        User isUserCreated = userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException((USER_NOT_FOUND_MSG)));
        isUserCreated.removeBookToUser(bookToRemove);
        User response = userRepository.save(isUserCreated);
        return ResponseEntity.ok(response);
    }
}
