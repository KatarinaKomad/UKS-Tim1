package uns.ac.rs.uks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.request.RegistrationRequest;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.exception.AlreadyExistsException;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.Role;
import uns.ac.rs.uks.model.RoleEnum;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.UserRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class CustomUserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
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

    @ParameterizedTest(name = "Finding user by email {0}")
    @ValueSource(strings = {Constants.ADMIN_EMAIL, Constants.PERA_EMAIL, Constants.MIKA_EMAIL})
    void testLoadUserByUsernameSuccess(String email) {
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @ParameterizedTest(name = "Not finding user by email {0}")
    @ValueSource(strings = {"test@gmail.com", "pera"})
    public void noUserWithEmail(String email) {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));
    }

}
