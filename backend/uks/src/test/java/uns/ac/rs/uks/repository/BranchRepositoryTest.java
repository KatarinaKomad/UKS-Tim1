package uns.ac.rs.uks.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.Branch;
import uns.ac.rs.uks.model.Issue;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("test")
public class BranchRepositoryTest {

    @Autowired
    private BranchRepository branchRepository;

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
}
