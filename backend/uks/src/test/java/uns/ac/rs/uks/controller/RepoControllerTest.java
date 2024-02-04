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
import uns.ac.rs.uks.dto.request.EditRepoRequest;
import uns.ac.rs.uks.dto.request.RepoForkRequest;
import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.dto.response.UserDTO;
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
//        ParameterizedTypeReference<List<RepoBasicInfoDTO>> responseType = new ParameterizedTypeReference<>() {};
//        ResponseEntity<?> responseEntity =
//                restTemplate.exchange("/repo/getAllPublic", HttpMethod.GET,  entity, responseType);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        List<RepoBasicInfoDTO> repos = (List<RepoBasicInfoDTO>) responseEntity.getBody();
//        assertNotNull(repos);
//        for (RepoBasicInfoDTO repo: repos) {
//            assertTrue(repo.getIsPublic());
//        }
    }

    @Test
    public void testGetMyRepos() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<String> entity = new HttpEntity<>(headers);
//        String url = "/repo/getMyRepos/" + Constants.MIKA_USER_ID;
//        ParameterizedTypeReference<List<RepoBasicInfoDTO>> responseType = new ParameterizedTypeReference<>() {};
//        ResponseEntity<?> responseEntity =
//                restTemplate.exchange(url, HttpMethod.GET,  entity, responseType);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        List<RepoBasicInfoDTO> repos = (List<RepoBasicInfoDTO>) responseEntity.getBody();
//        assertNotNull(repos);
//        for (RepoBasicInfoDTO repo: repos) {
//            assertEquals(repo.getOwner().getId(), Constants.MIKA_USER_ID);
//        }
    }

//    @Test
//    public void createNewRepo() {
//        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
//
//        String testName= "testName";
//        RepoRequest repoRequest = new RepoRequest();
//        repoRequest.setName(testName);
//        repoRequest.setOwnerId(Constants.MIKA_USER_ID);
//        repoRequest.setIsPublic(true);
//
//        HttpEntity<RepoRequest> entity = new HttpEntity<>(repoRequest,headers);
//
//        ResponseEntity<RepoBasicInfoDTO> responseEntity = restTemplate
//                .exchange("/repo/create", HttpMethod.POST, entity, RepoBasicInfoDTO.class);
//
//        assertNotNull(responseEntity.getBody());
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(testName, responseEntity.getBody().getName());
//        assertEquals(Constants.MIKA_USER_ID, responseEntity.getBody().getOwner().getId());
//    }

//    @Test
//    public void testSearchByNamePublic() {
//        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
//
//        String publicName = "UKS-test-PUBLIC";
//        RepoRequest repoRequest = new RepoRequest();
//        repoRequest.setName(publicName);
//        repoRequest.setOwnerId(Constants.MIKA_USER_ID);
//
//        HttpEntity<RepoRequest> entity = new HttpEntity<>(repoRequest,headers);
//
//        ResponseEntity<RepoBasicInfoDTO> responseEntity = restTemplate
//                .exchange("/repo/validateOverviewByRepoName", HttpMethod.POST, entity, RepoBasicInfoDTO.class);
//
//        assertNotNull(responseEntity.getBody());
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(publicName, responseEntity.getBody().getName());
//        assertEquals(Constants.PERA_USER_ID, responseEntity.getBody().getOwner().getId());
//    }

    @Test
    public void testSearchByNamePrivateNotMember() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        String privateName = "myPrivateRepo";
        RepoRequest repoRequest = new RepoRequest();
        repoRequest.setName(privateName);
        repoRequest.setOwnerId(Constants.PERA_USER_ID);

        HttpEntity<RepoRequest> entity = new HttpEntity<>(repoRequest, headers);

        ResponseEntity<RepoBasicInfoDTO> responseEntity = restTemplate
                .exchange("/repo/validateOverviewByRepoName", HttpMethod.POST, entity, RepoBasicInfoDTO.class);

        assertNull(responseEntity.getBody());
    }

    @Test
    public void testUserCanEditRepoItems() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        EditRepoRequest repoRequest = new EditRepoRequest();
        repoRequest.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);
        repoRequest.setUserId(Constants.PERA_USER_ID);

        HttpEntity<EditRepoRequest> entity = new HttpEntity<>(repoRequest,headers);

        ResponseEntity<Boolean> responseEntity = restTemplate
                .exchange("/repo/canEditRepoItems", HttpMethod.POST, entity, Boolean.class);

        assertEquals(Boolean.TRUE, responseEntity.getBody());
    }

    @Test
    public void testUserCanNotEditRepoItems() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        EditRepoRequest repoRequest = new EditRepoRequest();
        repoRequest.setRepoId(Constants.REPOSITORY_ID_3_UKS_TEST);
        repoRequest.setUserId(Constants.PERA_USER_ID);

        HttpEntity<EditRepoRequest> entity = new HttpEntity<>(repoRequest,headers);

        ResponseEntity<Boolean> responseEntity = restTemplate
                .exchange("/repo/canEditRepoItems", HttpMethod.POST, entity, Boolean.class);

        assertEquals(Boolean.FALSE, responseEntity.getBody());
    }

    @Test
    public void testGetMembersByRepoId() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        String url = "/repo/getMembers/" + Constants.REPOSITORY_ID_1_UKS_TEST;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<UserDTO>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<UserDTO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void testFork() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        RepoForkRequest request = new RepoForkRequest();
        request.setIsPublic(true);
        request.setName("test");
        request.setOriginalRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);
        request.setOwnerId(Constants.MIKA_USER_ID);

        HttpEntity<RepoForkRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<RepoBasicInfoDTO> responseEntity = restTemplate
                .exchange("/repo/fork", HttpMethod.POST, entity, RepoBasicInfoDTO.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, responseEntity.getBody().getForkParent().getId());
    }

    @Test
    public void testGetAllForked() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<RepoBasicInfoDTO>> responseType = new ParameterizedTypeReference<>() {};
        String url = "/repo/getAllForked/" + Constants.REPOSITORY_ID_1_UKS_TEST;
        ResponseEntity<?> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET,  entity, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<RepoBasicInfoDTO> repos = (List<RepoBasicInfoDTO>) responseEntity.getBody();
        assertNotNull(repos);
        for (RepoBasicInfoDTO repo: repos) {
            assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, repo.getForkParent().getId());
        }
    }
}
