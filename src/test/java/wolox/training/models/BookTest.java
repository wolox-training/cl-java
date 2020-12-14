package wolox.training.models;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.PersistenceException;
import org.hibernate.PropertyValueException;
import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import wolox.training.repositories.BookRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
class BookTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void whenSaveBook_thenReturnBook(){
        //Given
        Book bookTest = new Book(null,"Mystery","Paolo", "image.png", "Sacred Stone", "-", "Salamander","2000", 250,"123456789");
        testEntityManager.persist(bookTest);
        testEntityManager.flush();

        //when
        Book bookSaved = bookRepository.save(bookTest);

        //then
        assertEquals(bookSaved.getGenre(),bookTest.getGenre());
        assertEquals(bookSaved.getPublisher(),bookTest.getPublisher());
        System.out.println("whenSaveBook passed");
    }

    @Test
    public void whenSaveBookWithGenreNull_thenReturnBook(){
        //Given
        Book bookTest = new Book(null,null,"Paolo", "image.png", "Sacred Stone", "-", "Salamander","2000", 250,"123456789");
        testEntityManager.persist(bookTest);
        testEntityManager.flush();

        //when
        Book bookSaved = bookRepository.save(bookTest);

        //then
        assertEquals(bookSaved.getAuthor(),bookTest.getAuthor());
        assertEquals(bookSaved.getTitle(),bookTest.getTitle());
        System.out.println("whenSaveBookWithGenreNull passed");
    }

    @Test
    public void whenFindByAuthor_thenReturnBook(){
        //Given
        Book bookTest = new Book(null,"Mystery","Paolo", "image.png", "Sacred Stone", "-", "Salamander","2000", 250,"123456789");
        testEntityManager.persist(bookTest);
        testEntityManager.flush();

        //when
        Book bookFound = bookRepository.findFirstByAuthor(bookTest.getAuthor()).get();

        //then
        assertEquals(bookFound.getAuthor(),bookTest.getAuthor());
        System.out.println("whenFinByAuthor passed");
    }


    @Test()
    public void whenSaveBookWithTitleNull_thenThrowException() throws ConstraintViolationException {
        //Given
        Book bookTest = new Book(null,"Mystery","David", "image.png", null, "-", "Salamander","2000", 250,"123456789");

        //when
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            bookRepository.save(bookTest);
            testEntityManager.flush();
        });

        //then
        assertTrue(exception instanceof ConstraintViolationException);
        System.out.println("whenSaveBookWithTitleNull passed");
    }

    @Test()
    public void whenSaveBookWithIsbnNull_thenThrowException() throws ConstraintViolationException {
        //Given
        Book bookTest = new Book(null,"Mystery","David", "image.png", "title", "-", "Salamander","2000", 250,null);

        //when
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            bookRepository.save(bookTest);
            testEntityManager.flush();
        });
        //then
        assertTrue(exception instanceof ConstraintViolationException);
        System.out.println("whenSaveBookWithIsbnNull passed");
    }

    @Test()
    public void whenSaveBookWithPagesZero_thenThrowException() throws org.hibernate.exception.ConstraintViolationException {
        //Given
        Book bookTest = new Book(null,"Mystery","David", "image.png", "title", "-", "Salamander","2000", 0,"123456789");

        //when
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            bookRepository.save(bookTest);
            testEntityManager.flush();
        });
        //then
        assertTrue(exception.getCause() instanceof org.hibernate.exception.ConstraintViolationException);
        System.out.println("whenSaveBookWithPagesZero passed");
    }
}
