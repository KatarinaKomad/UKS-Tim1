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
import uns.ac.rs.uks.dto.request.ChangeStateRequest;
import uns.ac.rs.uks.dto.request.MilestoneRequest;
import uns.ac.rs.uks.dto.response.MilestoneDTO;
import uns.ac.rs.uks.model.State;
import uns.ac.rs.uks.util.Constants;
import uns.ac.rs.uks.util.LoginUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class MilestoneControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllRepoMilestones() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<MilestoneDTO>> responseType = new ParameterizedTypeReference<>() {};
        String url = "/milestone/getAllRepoMilestones/" + Constants.REPOSITORY_ID_1_UKS_TEST;
        ResponseEntity<?> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET,  entity, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<MilestoneDTO> milestones = (List<MilestoneDTO>) responseEntity.getBody();
        assertNotNull(milestones);
        assertEquals(milestones.size(), 2);
    }

    @Test
    public void testCreateMilestone() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        MilestoneRequest milestoneRequest = new MilestoneRequest();
        milestoneRequest.setDescription("test description3");
        milestoneRequest.setName("test name3");
        milestoneRequest.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        HttpEntity<MilestoneRequest> entity = new HttpEntity<>(milestoneRequest,headers);

        ResponseEntity<MilestoneDTO> responseEntity = restTemplate
                .exchange("/milestone/create", HttpMethod.POST, entity, MilestoneDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("test name3", responseEntity.getBody().getName());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, responseEntity.getBody().getRepoId());
    }

    @Test
    public void testDeleteMilestone() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<MilestoneRequest> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/milestone/delete/1", HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateMilestone() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        MilestoneRequest milestoneRequest = new MilestoneRequest();
        milestoneRequest.setId(1L);
        milestoneRequest.setDescription("test description-new");
        milestoneRequest.setName("test name-new");
        milestoneRequest.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        HttpEntity<MilestoneRequest> entity = new HttpEntity<>(milestoneRequest,headers);

        ResponseEntity<MilestoneDTO> responseEntity = restTemplate
                .exchange("/milestone/update", HttpMethod.PUT, entity, MilestoneDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("test name-new", responseEntity.getBody().getName());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, responseEntity.getBody().getRepoId());
    }

    @Test
    public void testCloseMilestone() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        ChangeStateRequest changeStateRequest = new ChangeStateRequest();
        changeStateRequest.setId(1L);
        changeStateRequest.setState(State.CLOSE);

        HttpEntity<ChangeStateRequest> entity = new HttpEntity<>(changeStateRequest,headers);

        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/milestone/changeState", HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
