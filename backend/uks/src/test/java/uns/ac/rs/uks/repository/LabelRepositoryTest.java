package uns.ac.rs.uks.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.Issue;
import uns.ac.rs.uks.model.Label;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.util.Constants;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class LabelRepositoryTest {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private EntityManager entityManager;

    @ParameterizedTest(name = "Finding labels of repository by id {0}")
    @ValueSource(strings = {"a3826e27-77d8-465c-9d9f-87ccbb04ecaf"})
    public void findAllByRepoId(String repoId) {
        UUID id = UUID.fromString(repoId);
        List<Label> labels = labelRepository.findAllByRepositoryId(id);
        assertFalse(labels.isEmpty());
        for (Label label: labels) {
            assertEquals(label.getRepository().getId(), id);
        }
    }

    @ParameterizedTest(name = "Finding no labels of repository by id {0}")
    @ValueSource(strings = {"ba6dcc79-1444-4310-9e7d-9736def57f60"})
    public void findAllByOwnerIdNoRepo(String userId) {
        List<Label> labels = labelRepository.findAllByRepositoryId(UUID.fromString(userId));
        assertTrue(labels.isEmpty());
    }

    @Test
    public void testDeleteLabelRelations() {
        Issue issue = issueRepository.findById(Constants.ISSUE_ID_1).get();
        assertEquals(issue.getLabels().size(), 2);

        labelRepository.deleteLabelRelations(1L);

        entityManager.flush();
        entityManager.clear();

        Issue issue2 = issueRepository.findById(Constants.ISSUE_ID_1).get();
        assertEquals(issue2.getLabels().size(), 1);
    }

}
