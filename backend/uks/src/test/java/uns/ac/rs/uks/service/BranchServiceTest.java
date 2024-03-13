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
import uns.ac.rs.uks.dto.request.OriginTargetBranchRequest;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.BranchRepository;
import uns.ac.rs.uks.repository.repo.RepoRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BranchServiceTest {

    @InjectMocks
    private BranchService branchService;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private GitoliteService gitoliteService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private RepoRepository repoRepository;
    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }


    public Branch createDefaultBranch(Repo repo) {
        Branch branch = new Branch();
        branch.setName("main");
        branch.setRepository(repo);
        branchRepository.save(branch);
        return branch;
    }

    @Test
    void testCreateDefaultBranchSuccess(){
        Repo repo = createRepo("testRepo", Constants.MIKA_USER_ID, true);
        Branch branch = branchService.createDefaultBranch(repo, repo.getOwner());

        assertNotNull(branch);
        assertEquals(branch.getRepository().getId(), repo.getId());
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

    @Test
    void testGetBranchByIdSuccess() {
        // Mocking
        Long id = 1L;
        Branch branch = new Branch();
        branch.setId(id);
        when(branchRepository.findById(id)).thenReturn(Optional.of(branch));
        // Test
        Branch branch1 = branchService.getById(id);
        // Assertions
        assertNotNull(branch1);
        assertEquals(branch1.getId(), id);
    }

    @Test
    public void testNoBranchById() {
        // Mocking
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> branchService.getById(2L));
    }

    @Test
    void testGetAllRepoBranches() {
        Repo repository = new Repo();
        repository.setId(Constants.REPOSITORY_ID_1_UKS_TEST);
        repository.setName("UKS-test");
        Branch branch1 = new Branch();
        branch1.setRepository(repository);
        Branch branch2 = new Branch();
        branch2.setRepository(repository);

        when(repoRepository.findById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(Optional.of(repository));
        when(branchRepository.findAllByRepositoryId(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(List.of(branch1, branch2));
        when(gitoliteService.readRepoBranches(repository.getName())).thenReturn(new ArrayList<>());

        List<BranchDTO> branches = branchService.getRepoBranches(Constants.REPOSITORY_ID_1_UKS_TEST);
        assertEquals(branches.size(), 2);
    }

    @Test
    void testGetRepoBranchesCount() {
        Repo repository = new Repo();
        repository.setId(Constants.REPOSITORY_ID_1_UKS_TEST);
        Branch branch1 = new Branch();
        branch1.setRepository(repository);
        Branch branch2 = new Branch();
        branch2.setRepository(repository);

        when(branchRepository.countAllByRepositoryId(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(2L);

        Long count = branchService.getRepoBranchesCount(Constants.REPOSITORY_ID_1_UKS_TEST);
        assertEquals(count, 2L);
    }

    @Test
    public void testCreateNewBranch() throws NotFoundException {
        // Mocking
        User user = new User();
        user.setId(Constants.PERA_USER_ID);

        Repo repo = new Repo();
        repo.setId(Constants.REPOSITORY_ID_1_UKS_TEST);
        repo.setName("UKS-test");

        OriginTargetBranchRequest request = new OriginTargetBranchRequest();
        request.setOriginName("master");
        request.setTargetName("test");
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        Branch branch = new Branch();
        branch.setName("test");
        branch.setRepository(repo);
        branch.setUpdatedBy(user);


        when(repoRepository.findById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(Optional.of(repo));
        when(entityManager.getReference(User.class, Constants.PERA_USER_ID)).thenReturn(user);
        doNothing().when(gitoliteService).newBranch("UKS-test","master", "test");
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);

        // Test
        BranchDTO dto = branchService.newBranch(request, user);

        // Assertions
        assertNotNull(dto);
        assertEquals("test", dto.getName());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, dto.getRepoId());
    }

    @Test
    public void testRenameBranch() {
        // Mocking
        User user = new User();
        user.setId(Constants.PERA_USER_ID);

        Repo repo = new Repo();
        repo.setId(Constants.REPOSITORY_ID_1_UKS_TEST);
        repo.setName("UKS-test");

        OriginTargetBranchRequest request = new OriginTargetBranchRequest();
        request.setOriginName("master");
        request.setTargetName("test");
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        Branch branch = new Branch();
        branch.setName("test");
        branch.setRepository(repo);
        branch.setUpdatedBy(user);


        when(repoRepository.findById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(Optional.of(repo));
        when(branchRepository.findByRepositoryIdAndName(Constants.REPOSITORY_ID_1_UKS_TEST, "master")).thenReturn(branch);
        when(entityManager.getReference(User.class, Constants.PERA_USER_ID)).thenReturn(user);
        doNothing().when(gitoliteService).renameBranch("UKS-test","master", "test");
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);

        // Test
        BranchDTO dto = branchService.renameBranch(request, user);

        // Assertions
        assertNotNull(dto);
        assertEquals("test", dto.getName());
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, dto.getRepoId());
    }

}
