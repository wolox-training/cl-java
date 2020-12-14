package wolox.training.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.persistence.PersistenceException;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import wolox.training.repositories.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void whenSaveUser_thenReturnUser() {
        // Given
        User userTest = new User(null,"Username","David",LocalDate.parse("2004-09-25"), new ArrayList<>());
        testEntityManager.persist(userTest);
        testEntityManager.flush();

        //When
        User userSaved = userRepository.save(userTest);

        //Then
        assertEquals(userSaved.getUsername(),userTest.getUsername());
        assertEquals(userSaved.getName(),userTest.getName());
        System.out.println("whenSaveUser passed");
    }

    @Test
    void whenFindUserByUsername_thenReturnUser() {
        //Given
        User userTest = new User(null,"Username","David",LocalDate.parse("2004-09-25"), new ArrayList<>());
        testEntityManager.persist(userTest);
        testEntityManager.flush();;

        //When
        User userFound = userRepository.findByUsername(userTest.getUsername()).get();

        //Then
        assertEquals(userFound.getUsername(),userTest.getUsername());
        System.out.println("whenFindUserByUsername pass");
    }

    @Test
    void whenSaveUserWithNameNull_thenThrowException() throws javax.validation.ConstraintViolationException {
        //Given
        User userTest = new User(null, "iskandar",null,LocalDate.parse("1995-01-22"), new ArrayList<>());

        //When
        javax.validation.ConstraintViolationException exception = assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            userRepository.save(userTest);
            testEntityManager.flush();
        });

        //Then
        assertTrue(exception instanceof javax.validation.ConstraintViolationException);
        System.out.println("whenSaveUserWithNameNull passed");
    }

    @Test
    void whenSaveUserWithUsernameTaken_thenThrowException() throws ConstraintViolationException {
        //Given
        User userTest = new User(null, "iskandar","Carlos",LocalDate.parse("1995-01-22"), new ArrayList<>());

        //When
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            userRepository.save(userTest);
            testEntityManager.flush();
        });

        //Then
        assertTrue(exception.getCause() instanceof ConstraintViolationException);
        System.out.println("whenSaveUserWithUsernameTaken passed");
    }


    @Test
    void whenSaveUserWithBirthdayGraterThanActualDate_thenThrowException() throws IllegalArgumentException {
        //Given
        User userTest = new User(null,"Undefeated", "David", LocalDate.parse("2021-09-25"),new ArrayList<>());

        //When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userTest.setBirthdate(LocalDate.parse("2021-05-25"));
            testEntityManager.flush();
        });

        //Then
        assertTrue(exception instanceof IllegalArgumentException);
        System.out.println("whenSaveUserWithBirthdayGraterThanActualDate passed");
    }

}
