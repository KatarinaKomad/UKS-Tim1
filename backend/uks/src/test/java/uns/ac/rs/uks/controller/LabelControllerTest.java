package uns.ac.rs.uks.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.dto.request.LabelRequest;
import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.response.LabelDTO;
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
public class LabelControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllRepoLabels() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<LabelDTO>> responseType = new ParameterizedTypeReference<>() {};
        String url = "/label/getAllRepoLabels/" + Constants.REPOSITORY_ID_1_UKS_TEST;
        ResponseEntity<?> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET,  entity, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<LabelDTO> repos = (List<LabelDTO>) responseEntity.getBody();
        assertNotNull(repos);
        assertEquals(repos.size(), 4);
    }

    @Test
    public void testCreateLabel() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        LabelRequest labelRequest = new LabelRequest();
        labelRequest.setColor("#118d56");
        labelRequest.setDescription("test description3");
        labelRequest.setName("test name3");
        labelRequest.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        HttpEntity<LabelRequest> entity = new HttpEntity<>(labelRequest,headers);

        ResponseEntity<LabelDTO> responseEntity = restTemplate
                .exchange("/label/create", HttpMethod.POST, entity, LabelDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("test name3", responseEntity.getBody().getName());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, responseEntity.getBody().getRepoId());
    }

    @Test
    public void testDeleteLabel() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<LabelRequest> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/label/delete/1", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
