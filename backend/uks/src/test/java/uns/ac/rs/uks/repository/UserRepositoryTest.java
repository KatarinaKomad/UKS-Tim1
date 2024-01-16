package uns.ac.rs.uks.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.util.Constants;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @ParameterizedTest(name = "Finding user by email {0}")
    @ValueSource(strings = {"admin@gmail.com", "pera@gmail.com", "mika@gmail.com"})
    public void userWithEmailExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        assertEquals(email, user.getEmail());
    }

    @ParameterizedTest(name = "Not finding user by email {0}")
    @ValueSource(strings = {"test@gmail.com", "pera"})
    public void noUserWithName(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        assertTrue(optionalUser.isEmpty());
    }


    @Test
    @DisplayName("Test blocking user by admin")
    public void updateBlockedByAdminToTrue() {
        UUID userId = Constants.MIKA_USER_ID;
        boolean blocked = true;

        Optional<User> optionalUserBefore = userRepository.findById(userId);
        assertTrue(optionalUserBefore.isPresent());
        userRepository.updateBlockedByAdmin(userId, blocked);

        entityManager.flush();
        entityManager.clear();

        Optional<User> optionalUserAfter = userRepository.findById(userId);
        assertTrue(optionalUserAfter.isPresent());
        assertTrue(optionalUserAfter.get().getBlockedByAdmin());
    }

    @Test
    @DisplayName("Test unblocking user by admin")
    public void updateBlockedByAdminToFalse() {
        UUID userId = Constants.BLOCKED_USER_ID;
        boolean blocked = false;

        Optional<User> optionalUserBefore = userRepository.findById(userId);
        assertTrue(optionalUserBefore.isPresent());
        userRepository.updateBlockedByAdmin(userId, blocked);

        entityManager.flush();
        entityManager.clear();

        Optional<User> optionalUserAfter = userRepository.findById(userId);
        assertTrue(optionalUserAfter.isPresent());
        assertFalse(optionalUserAfter.get().getBlockedByAdmin());
    }

    @Test
    @DisplayName("Test deleting user by admin")
    public void updateDeletedByAdminToTrue() {
        UUID userId = Constants.DELETED_USER_ID;
        boolean deleted = true;

        Optional<User> optionalUserBefore = userRepository.findById(userId);
        assertTrue(optionalUserBefore.isPresent());
        userRepository.updateDeletedByAdmin(userId, deleted);

        entityManager.flush();
        entityManager.clear();

        Optional<User> optionalUserAfter = userRepository.findById(userId);
        assertTrue(optionalUserAfter.isPresent());
        assertTrue(optionalUserAfter.get().getDeleted());
    }
}