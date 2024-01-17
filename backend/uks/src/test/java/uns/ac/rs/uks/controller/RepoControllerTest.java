package uns.ac.rs.uks.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.util.Constants;
import uns.ac.rs.uks.util.LoginUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RepoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testGetAllPublic() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<RepoBasicInfoDTO>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<?> responseEntity =
                restTemplate.exchange("/repo/getAllPublic", HttpMethod.GET,  entity, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<RepoBasicInfoDTO> repos = (List<RepoBasicInfoDTO>) responseEntity.getBody();
        assertNotNull(repos);
        for (RepoBasicInfoDTO repo: repos) {
            assertTrue(repo.getIsPublic());
        }
    }

    @Test
    public void testGetMyRepos() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "/repo/getMyRepos/" + Constants.MIKA_USER_ID;
        ParameterizedTypeReference<List<RepoBasicInfoDTO>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<?> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET,  entity, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<RepoBasicInfoDTO> repos = (List<RepoBasicInfoDTO>) responseEntity.getBody();
        assertNotNull(repos);
        for (RepoBasicInfoDTO repo: repos) {
            assertEquals(repo.getOwner().getId(), Constants.MIKA_USER_ID);
        }
    }
}
