package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.request.RepoForkRequest;
import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.request.RepoUserRequest;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.dto.response.WatchStarResponseDTO;
import uns.ac.rs.uks.exception.NotAllowedException;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.repo.RepoRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Mock
    private BranchService branchService;
    @Mock
    private MemberService memberService;
    @Mock
    private GitoliteService gitoliteService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }


//    @Test
//    void testGetPublicRepos() {
//        Repo repo1 = new Repo();
//        repo1.setIsPublic(true);
//        Repo repo2 = new Repo();
//        repo2.setIsPublic(true);
//
//        when(repoRepository.findAllByIsPublicTrue()).thenReturn(List.of(repo1, repo2));
//
//        List<RepoBasicInfoDTO> publicRepos = repoService.getAllPublic();
//        assertEquals(publicRepos.size(), 2);
//        assertTrue(publicRepos.get(0).getIsPublic());
//        assertTrue(publicRepos.get(1).getIsPublic());
//    }


//    @ParameterizedTest(name = "Finding repositories owned by user with id {0}")
//    @ValueSource(strings = {"0e7f2a1d-49d0-44cd-8a01-4d40186f6f08", "ff1d6606-e1f5-4e26-8a32-a14800b42a27"})
//    void testGetMyRepos(String userId) {
//        UUID id = UUID.fromString(userId);
//
//        User owner = new User();
//        owner.setId(id);
//        Repo repo = new Repo();
//        repo.setOwner(owner);
//
//        when(repoRepository.findAllByOwnerId(id)).thenReturn(List.of(repo));
//
//        List<RepoBasicInfoDTO> myRepos = repoService.getMyRepos(id);
//        assertFalse(myRepos.isEmpty());
//        assertEquals(myRepos.get(0).getOwner().getId(), id);
//    }
//
//
//    @ParameterizedTest(name = "Finding no repositories owned by user with id {0}")
//    @ValueSource(strings = {"af409c2d-95e0-432e-a6fc-6ef55cb4430d"})
//    void testGetMyReposNoRepos(String userId) {
//        UUID id = UUID.fromString(userId);
//
//        when(repoRepository.findAllByOwnerId(id)).thenReturn(new ArrayList<>());
//
//        List<RepoBasicInfoDTO> myRepos = repoService.getMyRepos(id);
//        assertTrue(myRepos.isEmpty());
//    }

//    @Test
//    public void testGetByNamePublic() {
//        // Mocking
//        String testName = "testName";
//        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
//        Repo repo = createRepo(testName, Constants.MIKA_USER_ID, true);
//
//        when(repoRepository.findAllByName(testName)).thenReturn(List.of(repo));
//
//        // Test
//        RepoBasicInfoDTO dto = repoService.getByNameAndPublicOrMember(repoRequest);
//
//        // Assertions
//        assertNotNull(dto);
//        assertEquals(Constants.MIKA_USER_ID, dto.getOwner().getId());
//        assertEquals(testName, dto.getName());
//    }

//    @Test
//    public void testGetByNamePrivateButMember() {
//        // Mocking
//        String testName = "testName";
//        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
//        Repo repo = createRepo(testName, Constants.PERA_USER_ID, false);
//
//        User contributor = new User();
//        contributor.setId(Constants.MIKA_USER_ID);
//
//        Member member = new Member();
//        member.setRepositoryRole(RepositoryRole.CONTRIBUTOR);
//        member.setUser(contributor);
//        member.setRepository(repo);
//
//        when(repoRepository.findAllByName(testName)).thenReturn(List.of(repo));
//        when(memberService.findMemberByUserIdAndRepositoryId(Constants.MIKA_USER_ID, repo.getId())).thenReturn(member);
//        // Test
//        RepoBasicInfoDTO dto = repoService.getByNameAndPublicOrMember(repoRequest);
//
//        // Assertions
//        assertNotNull(dto);
//        assertEquals(Constants.PERA_USER_ID, dto.getOwner().getId());
//        assertEquals(testName, dto.getName());
//        assertFalse(dto.getIsPublic());
//    }
//
//    @Test
//    public void testGetByNameNotPublicOrMember() {
//        // Mocking
//        String testName = "testName";
//        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
//        Repo repo = createRepo(testName, Constants.PERA_USER_ID, false);
//
//
//        when(repoRepository.findAllByName(testName)).thenReturn(List.of(repo));
//        when(memberService.findMemberByUserIdAndRepositoryId(Constants.PERA_USER_ID, repo.getId())).thenReturn(any(Member.class));
//        // Test
//        RepoBasicInfoDTO dto = repoService.getByNameAndPublicOrMember(repoRequest);
//
//        // Assertions
//        assertNull(dto);
//    }
//
//    @Test
//    public void testGetByNameNoRepo() {
//        // Mocking
//        String testName = "testName";
//        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
//        when(repoRepository.findAllByName(testName)).thenReturn(new ArrayList<>());
//        // Test
//        RepoBasicInfoDTO dto = repoService.getByNameAndPublicOrMember(repoRequest);
//        // Assertions
//        assertNull(dto);
//    }

    @Test
    public void createNewRepo() throws NotFoundException {
        // Mocking
        String testName = "testName";
        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
        Repo repo = createRepo(testName, Constants.MIKA_USER_ID, true);

        Branch defaultBranch = new Branch();
        defaultBranch.setId(1L);
        defaultBranch.setRepository(repo);
        defaultBranch.setName("main");

        User user = new User();
        user.setId(Constants.MIKA_USER_ID);

        when(branchService.createDefaultBranch(any(Repo.class), any(User.class))).thenReturn(defaultBranch);
        when(userService.getById(Constants.MIKA_USER_ID)).thenReturn(user);
        when(repoRepository.save(any(Repo.class))).thenReturn(repo);
        when(gitoliteService.createRepo(any(String.class), any(String.class))).thenReturn(testName);

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

    @Test
    public void testGetByNamePublic() {
        // Mocking
        String testName = "testName";
        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
        Repo repo = createRepo(testName, Constants.MIKA_USER_ID, true);

        when(repoRepository.findAllByName(testName)).thenReturn(List.of(repo));

        // Test
        RepoBasicInfoDTO dto = repoService.getByNameAndPublicOrMember(repoRequest);

        // Assertions
        assertNotNull(dto);
        assertEquals(Constants.MIKA_USER_ID, dto.getOwner().getId());
        assertEquals(testName, dto.getName());
    }

    @Test
    public void testGetByNamePrivateButMember() {
        // Mocking
        String testName = "testName";
        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
        Repo repo = createRepo(testName, Constants.PERA_USER_ID, false);

        User contributor = new User();
        contributor.setId(Constants.MIKA_USER_ID);

        Member member = new Member();
        member.setRepositoryRole(RepositoryRole.CONTRIBUTOR);
        member.setUser(contributor);
        member.setRepository(repo);

        when(repoRepository.findAllByName(testName)).thenReturn(List.of(repo));
        when(memberService.findMemberByUserIdAndRepositoryId(Constants.MIKA_USER_ID, repo.getId())).thenReturn(member);
        // Test
        RepoBasicInfoDTO dto = repoService.getByNameAndPublicOrMember(repoRequest);

        // Assertions
        assertNotNull(dto);
        assertEquals(Constants.PERA_USER_ID, dto.getOwner().getId());
        assertEquals(testName, dto.getName());
        assertFalse(dto.getIsPublic());
    }

    @Test
    public void testGetByNameNotPublicOrMember() {
        // Mocking
        String testName = "testName";
        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
        Repo repo = createRepo(testName, Constants.PERA_USER_ID, false);


        when(repoRepository.findAllByName(testName)).thenReturn(List.of(repo));
        when(memberService.findMemberByUserIdAndRepositoryId(Constants.PERA_USER_ID, repo.getId())).thenReturn(any(Member.class));
        // Test
        RepoBasicInfoDTO dto = repoService.getByNameAndPublicOrMember(repoRequest);

        // Assertions
        assertNull(dto);
    }

    @Test
    public void testGetByNameNoRepo() {
        // Mocking
        String testName = "testName";
        RepoRequest repoRequest = createRepoRequest(testName, Constants.MIKA_USER_ID);
        when(repoRepository.findAllByName(testName)).thenReturn(new ArrayList<>());
        // Test
        RepoBasicInfoDTO dto = repoService.getByNameAndPublicOrMember(repoRequest);
        // Assertions
        assertNull(dto);
    }


    @Test
    public void testForkRepo() throws NotAllowedException {
        // Mocking
        String testName = "testName";

        RepoForkRequest request = new RepoForkRequest();
        request.setIsPublic(true);
        request.setName(testName);
        request.setOriginalRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);
        request.setOwnerId(Constants.MIKA_USER_ID);

        Repo oldRepo = createRepo(testName, Constants.PERA_USER_ID, true);
        Repo newRepo = createRepo(testName, Constants.MIKA_USER_ID, true);
        newRepo.setForkParent(oldRepo);

        User user = new User();
        user.setId(Constants.MIKA_USER_ID);
        when(repoRepository.findById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(Optional.of(oldRepo));
        when(entityManager.getReference(User.class, Constants.MIKA_USER_ID)).thenReturn(user);
        when(repoRepository.save(any(Repo.class))).thenReturn(newRepo);

        // Test
        RepoBasicInfoDTO dto = repoService.forkRepo(request);

        // Assertions
        assertNotNull(dto);
        assertEquals(Constants.MIKA_USER_ID, dto.getOwner().getId());
        assertEquals(Constants.PERA_USER_ID, dto.getForkParent().getOwner().getId());

    }

    @Test
    public void testWatchRepo() {
        // Mocking
        String testName = "testName";

        RepoUserRequest request = new RepoUserRequest();
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);
        request.setUserId(Constants.PERA_USER_ID);

        Repo repo = createRepo(testName, Constants.PERA_USER_ID, true);
        repo.setWatchers(new ArrayList<>());

        User user = new User();
        user.setId(Constants.PERA_USER_ID);
        user.setWatching(new ArrayList<>());

        when(repoRepository.findById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(Optional.of(repo));
        when(userService.getById(Constants.PERA_USER_ID)).thenReturn(user);
        when(repoRepository.save(any(Repo.class))).thenReturn(repo);

        // Test
        RepoBasicInfoDTO dto = repoService.watchRepo(request);

        // Assertions
        assertNotNull(dto);
        assertEquals(1, dto.getWatchCount());
    }

    @Test
    public void testStarRepo()  {
        // Mocking
        String testName = "testName";

        RepoUserRequest request = new RepoUserRequest();
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);
        request.setUserId(Constants.PERA_USER_ID);

        Repo repo = createRepo(testName, Constants.PERA_USER_ID, true);
        repo.setStaredBy(new ArrayList<>());

        User user = new User();
        user.setId(Constants.PERA_USER_ID);
        user.setStared(new ArrayList<>());

        when(repoRepository.findById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(Optional.of(repo));
        when(userService.getById(Constants.PERA_USER_ID)).thenReturn(user);
        when(repoRepository.save(any(Repo.class))).thenReturn(repo);

        // Test
        RepoBasicInfoDTO dto = repoService.starRepo(request);

        // Assertions
        assertNotNull(dto);
        assertEquals(1, dto.getStarCount());
    }

    @Test
    public void testAmIWatchingStargazing()  {
        // Mocking
        String testName = "testName";

        RepoUserRequest request = new RepoUserRequest();
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);
        request.setUserId(Constants.PERA_USER_ID);

        Repo repo = createRepo(testName, Constants.PERA_USER_ID, true);
        User user = new User();
        user.setId(Constants.PERA_USER_ID);

        user.setStared(List.of(repo));
        repo.setStaredBy(List.of(user));
        user.setWatching(List.of(repo));
        repo.setWatchers(List.of(user));

        when(repoRepository.findById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(Optional.of(repo));

        // Test
        WatchStarResponseDTO dto = repoService.amIWatchingStargazing(request);

        // Assertions
        assertNotNull(dto);
        assertTrue(dto.isStargazing());
        assertTrue(dto.isWatching());
    }

    private Repo createRepo(String name, UUID userId, boolean isPublic) {
        Repo repo = new Repo();
        repo.setName(name);
        repo.setId(UUID.randomUUID());
        User user = new User();
        user.setId(userId);
        repo.setOwner(user);
        repo.setIsPublic(isPublic);
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
