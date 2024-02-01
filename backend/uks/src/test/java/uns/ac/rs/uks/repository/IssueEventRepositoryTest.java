package uns.ac.rs.uks.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.IssueEvent;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class IssueEventRepositoryTest {

    @Autowired
    private IssueEventRepository issueEventRepository;

    @ParameterizedTest(name = "Finding events of issue by id {0}")
    @ValueSource(strings = {"4822a7d1-5a79-4444-9065-256643c80ffc", "9d75ceda-974a-4e04-88dc-9c6e455ddcd1"})
    public void findAllEventsByIssueId(String issueId) {
        UUID id = UUID.fromString(issueId);
        List<IssueEvent> events = issueEventRepository.findAllByIssueId(id);
        assertFalse(events.isEmpty());
        for (IssueEvent event: events) {
            assertEquals(event.getIssue().getId(), id);
        }
    }

    @ParameterizedTest(name = "Finding no events of issue by id {0}")
    @ValueSource(strings = {"ba6dcc79-1444-4310-9e7d-9736def57f60"})
    public void findAllByIssueIdNoEvents(String issueId) {
        List<IssueEvent> events = issueEventRepository.findAllByIssueId(UUID.fromString(issueId));
        assertTrue(events.isEmpty());
    }

}
