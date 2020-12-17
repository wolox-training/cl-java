package wolox.training.models;

import static org.junit.jupiter.api.Assertions.*;
import static wolox.training.contants.ConstantsTest.NAME_SECOND_USER_TEST;
import static wolox.training.contants.ConstantsTest.NAME_USER_TEST;
import static wolox.training.contants.ConstantsTest.PASSWORD_USER_TEST;
import static wolox.training.contants.ConstantsTest.USERNAME_SECOND_USER_TEST;
import static wolox.training.contants.ConstantsTest.USERNAME_USER_TEST;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import wolox.training.repositories.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    private User userTest;

    @BeforeEach
    void setUp() {
        userTest = new User(null,USERNAME_SECOND_USER_TEST,NAME_USER_TEST,PASSWORD_USER_TEST,LocalDate.parse("2004-09-25"), new ArrayList<>());

    }

    @Test
    void whenSaveUser_thenReturnUser() {
        // Given
        testEntityManager.persist(userTest);
        testEntityManager.flush();

        //When
        User userSaved = userRepository.save(userTest);

        //Then
        assertEquals(userSaved.getUsername(),userTest.getUsername());
        assertEquals(userSaved.getName(),userTest.getName());
    }

    @Test
    void whenFindUserByUsername_thenReturnUser() {
        //Given
        testEntityManager.persist(userTest);
        testEntityManager.flush();;

        //When
        User userFound = userRepository.findByUsername(userTest.getUsername()).get();

        //Then
        assertEquals(userFound.getUsername(),userTest.getUsername());
    }

    @Test
    void whenSaveUserWithNameNull_thenThrowException() throws javax.validation.ConstraintViolationException {
        //Given
        User userTestNameNull = new User(null, USERNAME_USER_TEST,null,PASSWORD_USER_TEST,LocalDate.parse("1995-01-22"), new ArrayList<>());

        //When
        javax.validation.ConstraintViolationException exception = assertThrows(javax.validation.ConstraintViolationException.class, () -> {
            userRepository.save(userTestNameNull);
            testEntityManager.flush();
        });

        //Then
        assertTrue(exception instanceof javax.validation.ConstraintViolationException);
    }

    @Test
    void whenSaveUserWithUsernameTaken_thenThrowException() throws ConstraintViolationException {
        //Given
        userTest.setUsername(USERNAME_USER_TEST);
        //When
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            userRepository.save(userTest);
            testEntityManager.flush();
        });

        //Then
        assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }


    @Test
    void whenSaveUserWithBirthdayGraterThanActualDate_thenThrowException() throws IllegalArgumentException {
        //When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userTest.setBirthdate(LocalDate.parse("2021-05-25"));
            testEntityManager.flush();
        });

        //Then
        assertTrue(exception instanceof IllegalArgumentException);
    }

}
