package uns.ac.rs.uks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.request.LoginRequest;
import uns.ac.rs.uks.dto.request.PasswordResetRequest;
import uns.ac.rs.uks.dto.request.PasswordUpdateRequest;
import uns.ac.rs.uks.dto.request.RegistrationRequest;
import uns.ac.rs.uks.dto.response.TokenResponse;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.exception.AlreadyExistsException;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.Role;
import uns.ac.rs.uks.model.RoleEnum;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.security.TokenProvider;
import uns.ac.rs.uks.util.Constants;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;
    private AutoCloseable closeable;

    private final String testEmail = "test@example.com";
    private final String testPassword = "bWlrYTEyMw=="; //mika123

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }


    @Test
    void testRegisterUserSuccess() {
        // Mocking
        RegistrationRequest registrationRequest = createRegistrationRequest(testEmail, testPassword);
        Role role = createRole(1L, RoleEnum.ROLE_USER.getName());

        User user = new User();
        user.setEmail(testEmail);

        when(userService.existsByEmail(testEmail)).thenReturn(false);
        when(roleService.getRoleByName(any(String.class))).thenReturn(role);
        when(userService.save(any(User.class))).thenReturn(user);

        // Test
        UserDTO userDTO = authService.registerUser(registrationRequest);

        // Assertions
        assertNotNull(userDTO);
        assertEquals(testEmail, userDTO.getEmail());
    }

    @Test
    void testRegisterUserUserAlreadyExists(){
        // Mocking
        RegistrationRequest registrationRequest = createRegistrationRequest(testEmail, testPassword);

        when(userService.existsByEmail(testEmail)).thenReturn(true);

        // Test && Assertions
        assertThrows(AlreadyExistsException.class, () -> authService.registerUser(registrationRequest));
    }

    @Test
    void testLoginSuccess() {
        // Mocking
        LoginRequest loginRequest = new LoginRequest(Constants.MIKA_EMAIL,  Constants.MIKA_PASSWORD); //mika123
        String accessToken = "mockToken";
        long expiresAt = System.currentTimeMillis() + 3600000;

        User user = User.builder().email(Constants.MIKA_EMAIL).blockedByAdmin(false).deleted(false).build();

        when(userService.getUserByEmail(Constants.MIKA_EMAIL)).thenReturn(user);
        when(tokenProvider.generateToken(any())).thenReturn(accessToken);
        when(tokenProvider.getExpirationDateFromToken(any())).thenReturn(new Date(expiresAt));

        // Test
        TokenResponse tokenResponse = authService.login(loginRequest);
        // Assertions
        assertNotNull(tokenResponse);
        assertEquals(accessToken, tokenResponse.getAccessToken());
        assertEquals(expiresAt, tokenResponse.getExpiresAt());
    }

    @Test
    void testLoginWhenEmailDoesNotExists() {
        // Mocking
        LoginRequest loginRequest = new LoginRequest(testEmail,  Constants.MIKA_PASSWORD); //mika123
        when(userService.getUserByEmail(testEmail)).thenThrow(NotFoundException.class);
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testLoginWhenAccountDeleted() {
        // Mocking
        LoginRequest loginRequest = new LoginRequest(Constants.MIKA_EMAIL,  Constants.MIKA_PASSWORD); //mika123
        User user = User.builder().email(Constants.MIKA_EMAIL).blockedByAdmin(false).deleted(true).build();
        when(userService.getUserByEmail(Constants.MIKA_EMAIL)).thenReturn(user);
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testLoginWhenAccountBlocked() {
        LoginRequest loginRequest = new LoginRequest(Constants.MIKA_EMAIL,  Constants.MIKA_PASSWORD); //mika123
        User user = User.builder().email(Constants.MIKA_EMAIL).blockedByAdmin(true).deleted(false).build();
        when(userService.getUserByEmail(Constants.MIKA_EMAIL)).thenReturn(user);
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    void testResetPassword() {
        // Mocking
        String oldPassword = Constants.MIKA_PASSWORD;

        User user = User.builder()
                        .email(Constants.MIKA_EMAIL).blockedByAdmin(false).deleted(false).password(oldPassword)
                        .build();

        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail(Constants.MIKA_EMAIL);

        when(userService.getUserByEmail(Constants.MIKA_EMAIL)).thenReturn(user);
        when(userService.save(any(User.class))).thenReturn(any(User.class));

        // Test
        authService.resetPassword(request);
        // Assertions
        verify(emailService).sendResetPasswordEmail(any(User.class), any(String.class));
    }

    @Test
    void testUpdatePassword() {
        // Mocking
        String oldPassword = Constants.MIKA_PASSWORD;
        String newPassword = "cGVyYTEyMzQ=";
        String encodedNewPassword = "$2a$12$BUZ/Z5ZYrjyatiq9OdyHk.H2aY/KIgjgj9QuVPffj1zObzuX6Tmt.";
        String encodedOldPassword = "$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK";

        User userOld = User.builder()
                .email(Constants.MIKA_EMAIL).blockedByAdmin(false).deleted(false).password(encodedOldPassword)
                .build();

        User userNew = User.builder()
                .email(Constants.MIKA_EMAIL).blockedByAdmin(false).deleted(false).password(encodedNewPassword)
                .build();

        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setEmail(Constants.MIKA_EMAIL);
        request.setCurrentPassword(oldPassword);
        request.setPassword(newPassword);
        request.setPasswordConfirmation(newPassword);

        when(userService.getUserByEmail(Constants.MIKA_EMAIL)).thenReturn(userOld);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(userService.save(any(User.class))).thenReturn(userNew);
        doNothing().when(emailService).sendResetPasswordEmail(any(User.class), any(String.class));

        // Test
        authService.updatePassword(request);
        // Assertions
    }


    private Role createRole(Long id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }

    private RegistrationRequest createRegistrationRequest(String email, String base64Password) {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(email);
        registrationRequest.setPassword(base64Password);

        return registrationRequest;
    }
}
