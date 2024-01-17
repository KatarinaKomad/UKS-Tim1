package uns.ac.rs.uks.controller;

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
import uns.ac.rs.uks.util.Constants;
import uns.ac.rs.uks.util.LoginUtil;

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

        @Test
        public void testLoginSuccess() {
                LoginRequest request = new LoginRequest();
                request.setEmail(Constants.MIKA_EMAIL);
                request.setPassword(Constants.MIKA_PASSWORD);
                ResponseEntity<TokenResponse> responseEntity = restTemplate
                                .exchange("/auth/login", HttpMethod.POST, new HttpEntity<>(request),
                                                TokenResponse.class);

                assertNotNull(responseEntity.getBody());
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        public void testRegistrationSuccess() {
                RegistrationRequest request = new RegistrationRequest();
                request.setEmail("test@gmail.com");
                request.setPassword("dGVzdHBhc3N3b3Jk"); // testpassword
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
                request.setPassword(Constants.MIKA_PASSWORD);

                ResponseEntity<UserDTO> responseEntity = restTemplate
                                .exchange("/auth/register", HttpMethod.POST, new HttpEntity<>(request), UserDTO.class);

                assertNotNull(responseEntity.getBody());
                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        public void testMe() {
                HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<UserDTO> responseEntity = restTemplate.exchange("/auth/me", HttpMethod.GET, entity,
                                UserDTO.class);

                assertNotNull(responseEntity.getBody());
                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals(Constants.MIKA_EMAIL, responseEntity.getBody().getEmail());
        }
}
