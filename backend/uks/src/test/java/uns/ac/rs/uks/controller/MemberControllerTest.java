package uns.ac.rs.uks.controller;

import org.apache.tomcat.util.bcel.Const;
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
import uns.ac.rs.uks.dto.request.ChangeMemberRoleRequest;
import uns.ac.rs.uks.dto.request.RepoUserRequest;
import uns.ac.rs.uks.dto.response.RepoMemberDTO;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.model.MemberInviteStatus;
import uns.ac.rs.uks.model.RepositoryRole;
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
public class MemberControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetMembersByRepoId() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);
        String url = "/member/getMembers/" + Constants.REPOSITORY_ID_1_UKS_TEST;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<List<UserDTO>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<UserDTO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
    }

    @Test
    public void testInviteMemberToRepo() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        RepoUserRequest request = new RepoUserRequest();
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);
        request.setUserId(Constants.MIKA_USER_ID);

        String url = "/member/inviteUser";
        HttpEntity<RepoUserRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<RepoMemberDTO> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, RepoMemberDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MemberInviteStatus.PENDING, responseEntity.getBody().getInviteStatus());
    }

    @Test
    public void testAcceptInvitation() {
        RepoUserRequest request = new RepoUserRequest();
        request.setRepoId(Constants.REPOSITORY_ID_2_UKS_TEST);
        request.setUserId(Constants.MIKA_USER_ID);

        String link = "62613664636337392d313434342d343331302d396537642d3937333664656635376636303b306537663261316" +
                "42d343964302d343463642d386130312d346434303138366636663038?repoName=%27UKS-test-PUBLIC%27";
        String url = "/member/acceptInvitation/" + link;
        HttpEntity<RepoUserRequest> entity = new HttpEntity<>(request);
        ResponseEntity<RepoMemberDTO> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, RepoMemberDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MemberInviteStatus.ACCEPTED, responseEntity.getBody().getInviteStatus());
    }

    @Test
    public void testRemoveMember() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        RepoUserRequest request = new RepoUserRequest();
        request.setRepoId(Constants.REPOSITORY_ID_2_UKS_TEST);
        request.setUserId(Constants.PERA_USER_ID);

        String url = "/member/removeMember";
        HttpEntity<RepoUserRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<RepoMemberDTO> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, RepoMemberDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testChangeRole() {
        HttpHeaders headers = LoginUtil.login(Constants.MIKA_EMAIL, Constants.MIKA_PASSWORD, restTemplate);

        ChangeMemberRoleRequest request = new ChangeMemberRoleRequest();
        request.setRepoId(Constants.REPOSITORY_ID_2_UKS_TEST);
        request.setUserId(Constants.PERA_USER_ID);
        request.setRepositoryRole(RepositoryRole.VIEWER);

        String url = "/member/changeRole";
        HttpEntity<ChangeMemberRoleRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<RepoMemberDTO> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, RepoMemberDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(RepositoryRole.VIEWER, responseEntity.getBody().getRepositoryRole());
    }
}
