package wolox.training.models;

import static org.junit.jupiter.api.Assertions.*;
import static wolox.training.contants.ConstantsTest.AUTHOR_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.GENRE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.IMAGE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.ISBN_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.PAGES_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.SUBTITLE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.TITLE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.YEAR_BOOK_TEST;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import wolox.training.repositories.BookRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
class BookTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book bookTest;
    private Book bookTestSecond;

    @BeforeEach
    void setUp() {
        bookTest = new Book(null,GENRE_BOOK_TEST,AUTHOR_BOOK_TEST, IMAGE_BOOK_TEST, TITLE_BOOK_TEST,
                SUBTITLE_BOOK_TEST, SUBTITLE_BOOK_TEST,YEAR_BOOK_TEST, PAGES_BOOK_TEST,ISBN_BOOK_TEST);

        bookTestSecond = new Book(null,GENRE_BOOK_TEST,AUTHOR_BOOK_TEST, IMAGE_BOOK_TEST, TITLE_BOOK_TEST,
                SUBTITLE_BOOK_TEST, SUBTITLE_BOOK_TEST,YEAR_BOOK_TEST, PAGES_BOOK_TEST,ISBN_BOOK_TEST);
    }

    @Test
    public void whenSaveBook_thenReturnBook(){
        //Given
        testEntityManager.persist(bookTest);
        testEntityManager.flush();

        //when
        Book bookSaved = bookRepository.save(bookTest);

        //then
        assertEquals(bookSaved.getGenre(),bookTest.getGenre());
        assertEquals(bookSaved.getPublisher(),bookTest.getPublisher());
    }

    @Test
    public void whenSaveBookWithGenreNull_thenReturnBook(){
        //Given
        bookTest.setGenre(null);
        testEntityManager.persist(bookTest);
        testEntityManager.flush();

        //when
        Book bookSaved = bookRepository.save(bookTest);

        //then
        assertEquals(bookSaved.getAuthor(),bookTest.getAuthor());
        assertEquals(bookSaved.getTitle(),bookTest.getTitle());
    }

    @Test
    public void whenFindByAuthor_thenReturnBook(){
        //Given
        testEntityManager.persist(bookTest);
        testEntityManager.flush();

        //when
        Book bookFound = bookRepository.findFirstByAuthor(bookTest.getAuthor()).get();

        //then
        assertEquals(bookFound.getAuthor(),bookTest.getAuthor());
    }


    @Test()
    public void whenSaveBookWithTitleNull_thenThrowException() throws ConstraintViolationException {
        //Given
        Book bookTestTitleNull = new Book(null,GENRE_BOOK_TEST,AUTHOR_BOOK_TEST, IMAGE_BOOK_TEST, null,
                SUBTITLE_BOOK_TEST, SUBTITLE_BOOK_TEST,YEAR_BOOK_TEST, PAGES_BOOK_TEST,ISBN_BOOK_TEST);
        //when
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            bookRepository.save(bookTestTitleNull);
            testEntityManager.flush();
        });

        //then
        assertTrue(exception instanceof ConstraintViolationException);
    }

    @Test()
    public void whenSaveBookWithIsbnNull_thenThrowException() throws ConstraintViolationException {
        //Given
        Book bookTestIsbnNull = new Book(null,GENRE_BOOK_TEST,AUTHOR_BOOK_TEST, IMAGE_BOOK_TEST, TITLE_BOOK_TEST,
                SUBTITLE_BOOK_TEST, SUBTITLE_BOOK_TEST,YEAR_BOOK_TEST, PAGES_BOOK_TEST,null);
        //when
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            bookRepository.save(bookTestIsbnNull);
            testEntityManager.flush();
        });
        //then
        assertTrue(exception instanceof ConstraintViolationException);
    }

    @Test()
    public void whenSaveBookWithPagesZero_thenThrowException() throws IllegalArgumentException{
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookTest.setPages(0);
            testEntityManager.flush();
        });
        //then
        assertTrue(exception instanceof IllegalArgumentException);
    }

    @Test
    public void whenFindByPublisherGenreAndYear_thenReturnListBook(){
        //Given
        List<Book> booksTest = new ArrayList<>();
        testEntityManager.persist(bookTest);
        booksTest.add(bookTest);
        testEntityManager.persist(bookTestSecond);
        booksTest.add(bookTestSecond);
        testEntityManager.flush();

        //when
        List<Book> booksFound = bookRepository.findByPublisherAndGenreAndYear(
                bookTest.getPublisher(), bookTest.getGenre(), bookTest.getYear());

        //then
        assertEquals(booksFound,booksTest);
    }
}
