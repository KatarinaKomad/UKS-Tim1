package uns.ac.rs.uks.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.LabelRequest;
import uns.ac.rs.uks.dto.request.UserUpdateRequest;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.util.Constants;
import uns.ac.rs.uks.util.LoginUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testUpdateProfileInfo() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("test@gmail.com");
        request.setFirstName("Mika test name");
        request.setLastName("Mika test");
        request.setUsername(Constants.MIKA_EMAIL+"test");

        HttpEntity<UserUpdateRequest> entity = new HttpEntity<>(request,headers);

        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/profile/updateMyProfile/"+Constants.MIKA_USER_ID, HttpMethod.PUT, entity, UserDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getId(), Constants.MIKA_USER_ID);
        assertEquals(responseEntity.getBody().getEmail(), "test@gmail.com");
    }

    @Test
    public void testGetProfileInfo() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserDTO> responseEntity = restTemplate
                .exchange("/profile/getProfileInfo/"+Constants.MIKA_USER_ID, HttpMethod.GET, entity, UserDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getId(), Constants.MIKA_USER_ID);
    }

    @Test
    public void testDeleteAccount() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/profile/delete/"+Constants.MIKA_USER_ID, HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
