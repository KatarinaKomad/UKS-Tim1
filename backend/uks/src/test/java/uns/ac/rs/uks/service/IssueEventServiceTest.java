package uns.ac.rs.uks.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.response.IssueEventDTO;
import uns.ac.rs.uks.dto.transport.IssueItemsDTO;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.issue.IssueEventRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class IssueEventServiceTest {

    @InjectMocks
    private IssueEventService issueEventService;
    @Mock
    private IssueEventRepository issueEventRepository;
    private AutoCloseable closeable;
    @Test
    void testGetIssueEventHistory() {
        IssueEvent event1 = new IssueEvent();
        event1.setId(5L);
        IssueEvent event2 = new IssueEvent();
        event2.setId(6L);
        when(issueEventRepository.findAllByIssueId(Constants.ISSUE_ID_1)).thenReturn(List.of(event1, event2));

        List<IssueEventDTO> issues = issueEventService.getIssueEventHistory(Constants.ISSUE_ID_1);
        assertEquals(issues.size(), 2);
    }

    @Test
    void testCreateIssueEventsFromNewIssue() {
        IssueItemsDTO items = new IssueItemsDTO();

        Label label = new Label();
        label.setName("labelName");
        items.setLabels(List.of(label));

        User assignee = new User();
        assignee.setFirstName("Pera");
        assignee.setLastName("Perovic");
        items.setAssignees(List.of(assignee));

        Milestone milestone = new Milestone();
        milestone.setName("milestoneName");
        items.setMilestone(milestone);

        List<IssueEvent> events = issueEventService.createIssueEventsFromNewIssue(items);

        assertEquals(events.size(), 3);
        assertEquals(events.get(0).getType(), IssueEventType.LABEL);
        assertEquals(events.get(0).getNewValue(), "labelName");
        assertEquals(events.get(1).getType(), IssueEventType.ASSIGNEE);
        assertEquals(events.get(1).getNewValue(), "Pera Perovic");
        assertEquals(events.get(2).getType(), IssueEventType.MILESTONE);
        assertEquals(events.get(2).getNewValue(), "milestoneName");
    }
}
