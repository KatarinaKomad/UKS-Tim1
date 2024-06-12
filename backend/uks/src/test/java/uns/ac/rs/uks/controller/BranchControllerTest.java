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
import uns.ac.rs.uks.dto.request.IssueRequest;
import uns.ac.rs.uks.dto.request.OriginTargetBranchRequest;
import uns.ac.rs.uks.dto.request.TargetBranchRequest;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.dto.response.IssueDTO;
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
public class BranchControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testGetRepoBranchesCount() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "/branch/getRepoBranchesCount/" + Constants.REPOSITORY_ID_1_UKS_TEST;
        ResponseEntity<?> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET,  entity, Long.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Long count = (Long) responseEntity.getBody();
        assertNotNull(count);
        assertEquals(count, 2L);
    }

    @Test
    public void testDeleteBranch() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        TargetBranchRequest request = new TargetBranchRequest();
        request.setBranchName("dev");
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        HttpEntity<TargetBranchRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Void> responseEntity = restTemplate
                .exchange("/branch/delete", HttpMethod.POST, entity, Void.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testCreateBranch() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        OriginTargetBranchRequest request = new OriginTargetBranchRequest();
        request.setOriginName("master");
        request.setTargetName("test");
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        HttpEntity<OriginTargetBranchRequest> entity = new HttpEntity<>(request,headers);

        ResponseEntity<BranchDTO> responseEntity = restTemplate
                .exchange("/branch/newBranch", HttpMethod.POST, entity, BranchDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("test", responseEntity.getBody().getName());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, responseEntity.getBody().getRepoId());
    }


    @Test
    public void testRenameBranch() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        OriginTargetBranchRequest request = new OriginTargetBranchRequest();
        request.setOriginName("master");
        request.setTargetName("test");
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        HttpEntity<OriginTargetBranchRequest> entity = new HttpEntity<>(request,headers);

        ResponseEntity<BranchDTO> responseEntity = restTemplate
                .exchange("/branch/renameBranch", HttpMethod.POST, entity, BranchDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("test", responseEntity.getBody().getName());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, responseEntity.getBody().getRepoId());
    }
}
