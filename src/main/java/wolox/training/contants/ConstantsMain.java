package wolox.training.contants;

public final class ConstantsMain {

    private ConstantsMain(){

    }
    //BookController
    public static String BOOK_NOT_FOUND_MSG = "Book not found";
    public static String ID_MISMATCH_MSG = "Book Id mismatched";
    public static String SUCCESSFULLY_BOOK_DELETED_MSG = "Book Successfully Deleted";

    //UserController
    public static String USER_NOT_FOUND_MSG = "User not found";
    public static String ID_MISMATCHED_MSG = "User Id mismatched";
    public static String SUCCESSFULLY_USER_DELETED_MSG = "User Successfully Deleted";
    public static String USERNAME_TAKEN_MSG = "The username is already taken";

    //OpenLibraryService
    public static String URL_EXTERNAL_API = "https://openlibrary.org/api/books";
    public static String PARAM_STRING = "%1$s?bibkeys=ISBN:%2$s%3$s";
    public static String JSON_FORMAT_STRING = "&format=json&jscmd=data";
    public static String IMAGE_MSG = "Has no image";
    public static String ATTRIBUTE_CONFLICT_MSG = "An attribute has a conflict";

    //BookModel
    public static String AUTHOR_EMPTY_MSG = "Author must not be empty";
    public static String AUTHOR_NULL_MSG = "Author must not be null";
    public static String AUTHOR_INVALID_CHARACTERS_MSG = "Author must not have numbers or invalid characters";
    public static String IMAGE_NULL_MSG = "Image must not be null";
    public static String IMAGE_EMPTY_MSG = "Image must not be empty";
    public static String TITLE_NULL_MSG = "Title must not be null";
    public static String TITLE_EMPTY_MSG = "Title must not be empty";
    public static String SUBTITLE_NULL_MSG = "Subtitle must not be null";
    public static String SUBTITLE_EMPTY_MSG = "Subtitle must not be empty";
    public static String PUBLISHER_NULL_MSG = "Publisher must not be null";
    public static String PUBLISHER_EMPTY_MSG = "Publisher must not be empty";
    public static String YEAR_NULL_MSG = "Year must not be null";
    public static String YEAR_EMPTY_MSG = "Year must not be empty";
    public static String YEAR_BEFORE_MSG = "Year must be less or equal than actual";
    public static String PAGES_NULL_MSG = "Pages must not be null";
    public static String PAGES_ZERO_MSG = "Pages must be grater than zero";
    public static String ISBN_NULL_MSG = "Isbn must not be null";
    public static String ISBN_EMPTY_MSG = "Isbn must not be empty";

    //UserModel
    public static String USERNAME_NULL_MSG = "Username must not be null";
    public static String USERNAME_EMPTY_MSG = "Username must not be empty";
    public static String NAME_NULL_MSG = "Name must not be null";
    public static String NAME_EMPTY_MSG = "Name must not be empty";
    public static String NAME_INVALID_CHARACTERS_MSG = "Name must not have numbers or invalid characters";
    public static String PASSWORD_NULL_MSG = "Password must not be null";
    public static String PASSWORD_EMPTY_MSG = "Password must not be empty";
    public static String BIRTHDAY_NULL_MSG = "Birthday must not be null";
    public static String BIRTHDAY_BEFORE_MSG = "Birthday must be less than actual date";
}
