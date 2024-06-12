package uns.ac.rs.uks.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.Branch;
import uns.ac.rs.uks.model.Issue;
import uns.ac.rs.uks.util.Constants;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class BranchRepositoryTest {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private EntityManager entityManager;

    @ParameterizedTest(name = "Finding branches of repository by id {0}")
    @ValueSource(strings = {"a3826e27-77d8-465c-9d9f-87ccbb04ecaf"})
    public void findAllByRepositoryId(String repoId) {
        UUID id = UUID.fromString(repoId);
        List<Branch> branches = branchRepository.findAllByRepositoryId(id);
        assertFalse(branches.isEmpty());
        for (Branch branch: branches) {
            assertEquals(branch.getRepository().getId(), id);
        }
    }

    @ParameterizedTest(name = "Count branches of repository by id {0}")
    @ValueSource(strings = {"a3826e27-77d8-465c-9d9f-87ccbb04ecaf"})
    public void countAllByRepositoryId(String repoId) {
        UUID id = UUID.fromString(repoId);
        List<Branch> branches = branchRepository.findAllByRepositoryId(id);
        assertFalse(branches.isEmpty());
        assertEquals(branches.size(), 2);
    }

    @Test
    public void findByRepositoryIdAndName() {
        Branch branch = branchRepository.findByRepositoryIdAndName(Constants.REPOSITORY_ID_1_UKS_TEST, "master");
        assertNotNull(branch);
        assertEquals(branch.getName(), "master");
        assertEquals(branch.getRepository().getId(), Constants.REPOSITORY_ID_1_UKS_TEST);
    }

    @Test
    public void testDeleteLabelRelations() {
        Branch branchBefore = branchRepository.findByRepositoryIdAndName(Constants.REPOSITORY_ID_1_UKS_TEST, "master");
        assertNotNull(branchBefore);

        branchRepository.deleteByRepositoryIdAndName(Constants.REPOSITORY_ID_1_UKS_TEST, "master");

        entityManager.flush();
        entityManager.clear();

        Branch branchAfter = branchRepository.findByRepositoryIdAndName(Constants.REPOSITORY_ID_1_UKS_TEST, "master");
        assertNull(branchAfter);
    }

}
