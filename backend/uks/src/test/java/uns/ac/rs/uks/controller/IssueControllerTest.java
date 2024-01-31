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
import uns.ac.rs.uks.dto.request.IssueEventRequest;
import uns.ac.rs.uks.dto.request.IssueRequest;
import uns.ac.rs.uks.dto.response.IssueDTO;
import uns.ac.rs.uks.dto.response.IssueEventDTO;
import uns.ac.rs.uks.model.IssueEventType;
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
public class IssueControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAllRepoIssues() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<IssueDTO>> responseType = new ParameterizedTypeReference<>() {};
        String url = "/issue/getAllRepoIssues/" + Constants.REPOSITORY_ID_1_UKS_TEST;
        ResponseEntity<?> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET,  entity, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<IssueDTO> issues = (List<IssueDTO>) responseEntity.getBody();
        assertNotNull(issues);
        assertEquals(issues.size(), 2);
    }

    @Test
    public void testCreateIssue() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        IssueRequest issueRequest = new IssueRequest();
        issueRequest.setDescription("test description3");
        issueRequest.setName("test name3");
        issueRequest.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        HttpEntity<IssueRequest> entity = new HttpEntity<>(issueRequest,headers);

        ResponseEntity<IssueDTO> responseEntity = restTemplate
                .exchange("/issue/create", HttpMethod.POST, entity, IssueDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("test name3", responseEntity.getBody().getName());
        assertEquals("test description3", responseEntity.getBody().getDescription());
        assertEquals(State.OPEN, responseEntity.getBody().getState());
    }

    @Test
    public void testDeleteIssue() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<IssueRequest> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/issue/delete/" + Constants.ISSUE_ID_1, HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testIssueUpdateOfLabel() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        IssueEventRequest issueEventRequest = new IssueEventRequest();
        issueEventRequest.setIssueId(Constants.ISSUE_ID_1);
        issueEventRequest.setLabelIds(List.of(1L));
        issueEventRequest.setType(IssueEventType.LABEL);
        issueEventRequest.setAuthorId(Constants.MIKA_USER_ID);

        HttpEntity<IssueEventRequest> entity = new HttpEntity<>(issueEventRequest,headers);

        ResponseEntity<IssueDTO> responseEntity = restTemplate
                .exchange("/issue/update", HttpMethod.PUT, entity, IssueDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().getLabels().get(0).getId());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, responseEntity.getBody().getLabels().get(0).getRepoId());
    }

    @Test
    public void testIssueUpdateOfMilestone() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        IssueEventRequest issueEventRequest = new IssueEventRequest();
        issueEventRequest.setIssueId(Constants.ISSUE_ID_1);
        issueEventRequest.setMilestoneId(1L);
        issueEventRequest.setType(IssueEventType.MILESTONE);
        issueEventRequest.setAuthorId(Constants.MIKA_USER_ID);

        HttpEntity<IssueEventRequest> entity = new HttpEntity<>(issueEventRequest,headers);

        ResponseEntity<IssueDTO> responseEntity = restTemplate
                .exchange("/issue/update", HttpMethod.PUT, entity, IssueDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().getMilestone().getId());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, responseEntity.getBody().getLabels().get(0).getRepoId());
    }

    @Test
    public void testCloseIssue() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        IssueEventRequest issueEventRequest = new IssueEventRequest();
        issueEventRequest.setIssueId(Constants.ISSUE_ID_1);
        issueEventRequest.setState(State.CLOSE);
        issueEventRequest.setType(IssueEventType.STATE);
        issueEventRequest.setAuthorId(Constants.MIKA_USER_ID);

        HttpEntity<IssueEventRequest> entity = new HttpEntity<>(issueEventRequest,headers);

        ResponseEntity<IssueDTO> responseEntity = restTemplate
                .exchange("/issue/update", HttpMethod.PUT, entity, IssueDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(State.CLOSE, responseEntity.getBody().getState());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, responseEntity.getBody().getLabels().get(0).getRepoId());
    }

    @Test
    public void testGetIssueEventHistory() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<IssueEventDTO>> responseType = new ParameterizedTypeReference<>() {};
        String url = "/issue/getIssueEventHistory/" + Constants.ISSUE_ID_1;
        ResponseEntity<?> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET,  entity, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<IssueEventDTO> issues = (List<IssueEventDTO>) responseEntity.getBody();
        assertNotNull(issues);
        assertEquals(issues.size(), 2);
    }
}
