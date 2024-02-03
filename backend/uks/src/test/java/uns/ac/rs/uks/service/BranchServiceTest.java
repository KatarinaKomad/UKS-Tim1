package uns.ac.rs.uks.service;

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
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.BranchRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BranchServiceTest {

    @InjectMocks
    private BranchService branchService;
    @Mock
    private BranchRepository branchRepository;
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
        Branch branch = branchService.createDefaultBranch(repo);

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
}
