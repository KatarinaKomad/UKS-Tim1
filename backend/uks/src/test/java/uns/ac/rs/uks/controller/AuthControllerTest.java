package uns.ac.rs.uks.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.dto.request.LoginRequest;
import uns.ac.rs.uks.dto.request.RegistrationRequest;
import uns.ac.rs.uks.dto.response.TokenResponse;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.model.Role;
import uns.ac.rs.uks.model.RoleEnum;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.util.Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String mikaPassword = "bWlrYTEyMw==";

    private String token;

    HttpHeaders headers = new HttpHeaders();

    @AfterEach
    public void cleanUp() {
        token = "";
        headers.clear();
    }

    public void login(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(username);
        request.setPassword(password);
        ResponseEntity<TokenResponse> tokenResp = restTemplate
                .exchange("/auth/login", HttpMethod.POST, new HttpEntity<>(request), TokenResponse.class);

        assertNotNull(tokenResp.getBody());
        token = tokenResp.getBody().getAccessToken();
        headers.setBearerAuth(token);
    }

    @Test
    public void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setEmail(Constants.MIKA_EMAIL);
        request.setPassword(mikaPassword);
        ResponseEntity<TokenResponse> responseEntity = restTemplate
                .exchange("/auth/login", HttpMethod.POST, new HttpEntity<>(request), TokenResponse.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testRegistrationSuccess() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("dGVzdHBhc3N3b3Jk"); //testpassword
        request.setPasswordConfirmation("dGVzdHBhc3N3b3Jk");
        request.setFirstName("testName");
        request.setLastName("testLastName");

        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/auth/register", HttpMethod.POST, new HttpEntity<>(request), UserDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("test@gmail.com", responseEntity.getBody().getEmail());
    }

    @Test
    public void testRegisterUserUserAlreadyExists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail(Constants.MIKA_EMAIL);
        request.setPassword(mikaPassword);

        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/auth/register", HttpMethod.POST, new HttpEntity<>(request), UserDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }



    @Test
    public void testMe() {
        login(Constants.MIKA_EMAIL, mikaPassword);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDTO> responseEntity = restTemplate.exchange("/auth/me", HttpMethod.GET,  entity, UserDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.MIKA_EMAIL, responseEntity.getBody().getEmail());
    }
}
