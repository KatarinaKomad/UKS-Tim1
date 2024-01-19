package uns.ac.rs.uks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.UserRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void testGetUserByEmailSuccess() {
        // Mocking
        String email = Constants.MIKA_EMAIL;
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Test
        User user1 = userService.getUserByEmail(email);

        // Assertions
        assertNotNull(user1);
        assertEquals(user1.getEmail(), email);
    }

    @Test
    public void testNoUserByEmail() {
        // Mocking
        when(userRepository.findByEmail(Constants.MIKA_EMAIL)).thenReturn(Optional.empty());
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> userService.getUserByEmail(Constants.MIKA_EMAIL));
    }

    @Test
    void testFindUserByEmailToDTOSuccess() {
        // Mocking
        String email = Constants.MIKA_EMAIL;
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Test
        UserDTO userDTO = userService.findByEmail(email);

        // Assertions
        assertNotNull(userDTO);
        assertEquals(userDTO.getEmail(), email);
    }

    @Test
    public void testNoUserByEmailToDTO() {
        // Mocking
        when(userRepository.findByEmail(Constants.MIKA_EMAIL)).thenReturn(Optional.empty());
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> userService.getUserByEmail(Constants.MIKA_EMAIL));
    }


    @Test
    void testExistsByEmailSuccess() {
        // Mocking
        String email = Constants.MIKA_EMAIL;
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Test
        boolean userExists = userService.existsByEmail(email);
        // Assertions
        assertTrue(userExists);
    }

    @Test
    void testNotExistsByEmail() {
        // Mocking
        when(userRepository.findByEmail(Constants.MIKA_EMAIL)).thenReturn(Optional.empty());
        // Test
        boolean userExists = userService.existsByEmail(Constants.MIKA_EMAIL);
        // Assertions
        assertFalse(userExists);
    }


    @Test
    void testGetUserByIdSuccess() {
        // Mocking
        UUID id = Constants.MIKA_USER_ID;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // Test
        User user1 = userService.getById(id);

        // Assertions
        assertNotNull(user1);
        assertEquals(user1.getId(), id);
    }

    @Test
    public void testNoUserById() {
        // Mocking
        when(userRepository.findById(Constants.MIKA_USER_ID)).thenReturn(Optional.empty());
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> userService.getById(Constants.MIKA_USER_ID));
    }

}
