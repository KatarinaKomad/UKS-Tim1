package uns.ac.rs.uks.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.Issue;
import uns.ac.rs.uks.model.IssueEvent;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class IssueRepositoryTest {

    @Autowired
    private IssueRepository issueRepository;

    @ParameterizedTest(name = "Finding issues of repository by id {0}")
    @ValueSource(strings = {"a3826e27-77d8-465c-9d9f-87ccbb04ecaf"})
    public void findAllByRepositoryId(String repoId) {
        UUID id = UUID.fromString(repoId);
        List<Issue> issues = issueRepository.findAllByRepositoryId(id);
        assertFalse(issues.isEmpty());
        for (Issue issue: issues) {
            assertEquals(issue.getRepository().getId(), id);
        }
    }

    @ParameterizedTest(name = "Finding issues of repository by id {0}")
    @ValueSource(strings = {"ba6dcc79-1444-4310-9e7d-9736def57f60"})
    public void findAllByIssueIdNoEvents(String repoId) {
        List<Issue> issues = issueRepository.findAllByRepositoryId(UUID.fromString(repoId));
        assertTrue(issues.isEmpty());
    }

}
