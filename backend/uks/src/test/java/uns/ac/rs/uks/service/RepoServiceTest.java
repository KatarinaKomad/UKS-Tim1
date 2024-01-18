package uns.ac.rs.uks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.request.RegistrationRequest;
import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.exception.AlreadyExistsException;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.RepoMapper;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.Role;
import uns.ac.rs.uks.model.RoleEnum;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.RepoRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RepoServiceTest {
    @InjectMocks
    private RepoService repoService;
    @Mock
    private RepoRepository repoRepository;
    @Mock
    private UserService userService;
    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }


    @Test
    void testGetPublicRepos() {
        Repo repo1 = new Repo();
        repo1.setIsPublic(true);
        Repo repo2 = new Repo();
        repo2.setIsPublic(true);

        when(repoRepository.findAllByIsPublicTrue()).thenReturn(List.of(repo1, repo2));

        List<RepoBasicInfoDTO> publicRepos = repoService.getAllPublic();
        assertEquals(publicRepos.size(), 2);
        assertTrue(publicRepos.get(0).getIsPublic());
        assertTrue(publicRepos.get(1).getIsPublic());
    }


    @ParameterizedTest(name = "Finding repositories owned by user with id {0}")
    @ValueSource(strings = {"0e7f2a1d-49d0-44cd-8a01-4d40186f6f08", "ff1d6606-e1f5-4e26-8a32-a14800b42a27"})
    void testGetMyRepos(String userId) {
        UUID id = UUID.fromString(userId);

        User owner = new User();
        owner.setId(id);
        Repo repo = new Repo();
        repo.setOwner(owner);

        when(repoRepository.findAllByOwnerId(id)).thenReturn(List.of(repo));

        List<RepoBasicInfoDTO> myRepos = repoService.getMyRepos(id);
        assertFalse(myRepos.isEmpty());
        assertEquals(myRepos.get(0).getOwner().getId(), id);
    }


    @ParameterizedTest(name = "Finding no repositories owned by user with id {0}")
    @ValueSource(strings = {"af409c2d-95e0-432e-a6fc-6ef55cb4430d"})
    void testGetMyReposNoRepos(String userId) {
        UUID id = UUID.fromString(userId);

        when(repoRepository.findAllByOwnerId(id)).thenReturn(new ArrayList<>());

        List<RepoBasicInfoDTO> myRepos = repoService.getMyRepos(id);
        assertTrue(myRepos.isEmpty());
    }

    @Test
    public void createNewRepo() throws NotFoundException {
        // Mocking
        String testName = "testName";
        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
        Repo repo = createRepo(testName, Constants.MIKA_USER_ID);

        User user = new User();
        user.setId(Constants.MIKA_USER_ID);

        when(userService.getById(Constants.MIKA_USER_ID)).thenReturn(user);
        when(repoRepository.save(any(Repo.class))).thenReturn(repo);

        // Test
        RepoBasicInfoDTO dto = repoService.createNewRepo(repoRequest);

        // Assertions
        assertNotNull(dto);
        assertEquals(Constants.MIKA_USER_ID, dto.getOwner().getId());
        assertEquals(testName, dto.getName());

    }

    @Test
    void testAddNewRepoUserDoesNotExists(){
        // Mocking
        String testName = "testName";
        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);

        when(userService.getById(Constants.MIKA_USER_ID)).thenThrow(new NotFoundException("User not found!"));

        // Test && Assertions
        assertThrows(NotFoundException.class, () -> repoService.createNewRepo(repoRequest));
    }

    private Repo createRepo(String name, UUID id) {
        Repo repo = new Repo();
        repo.setName(name);
        User user = new User();
        user.setId(id);
        repo.setOwner(user);
        repo.setIsPublic(true);
        return repo;
    }

    private RepoRequest createRepoRequest(String name, UUID id) {
        RepoRequest repoRequest = new RepoRequest();
        repoRequest.setName(name);
        repoRequest.setOwnerId(id);
        repoRequest.setIsPublic(true);
        return repoRequest;
    }
}
