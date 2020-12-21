package wolox.training.repositories;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM Users WHERE (birthdate BETWEEN COALESCE(:begin, CAST('1753-01-01' AS DATE)) AND "
            + "COALESCE(:end , CAST('9999-12-31' AS DATE)) OR birthdate IS NULL) AND "
            + "(:chars IS NULL OR LOWER(name) LIKE LOWER(concat('%', :chars,'%')))",
            nativeQuery = true)
    List<User> findByBirthdateBetweenAndNameContainsIgnoreCase(
             @Param("begin") LocalDate begin,
             @Param("end") LocalDate end,
             @Param("chars") String charactersSequence);

}
