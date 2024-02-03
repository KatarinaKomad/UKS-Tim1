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
import uns.ac.rs.uks.dto.request.IssueEventRequest;
import uns.ac.rs.uks.dto.request.IssueRequest;
import uns.ac.rs.uks.dto.response.IssueDTO;
import uns.ac.rs.uks.dto.response.UserIssuesDTO;
import uns.ac.rs.uks.dto.transport.IssueItemsDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.IssueMapper;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.IssueRepository;
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
public class IssueServiceTest {


    @InjectMocks
    private IssueService issueService;
    @Mock
    private IssueRepository issueRepository;
    @Mock
    private IssueEventService issueEventService;
    @Mock
    private EntityManager entityManager;
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
    void testGetIssueByIdSuccess() {
        // Mocking
        UUID id = Constants.ISSUE_ID_1;
        Issue issue = new Issue();
        issue.setId(id);
        when(issueRepository.findById(id)).thenReturn(Optional.of(issue));
        // Test
        Issue issue1 = issueService.getById(id);
        // Assertions
        assertNotNull(issue1);
        assertEquals(issue1.getId(), id);
    }

    @Test
    public void testNoIssueById() {
        // Mocking
        when(issueRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> issueService.getById(UUID.randomUUID()));
    }

    @Test
    void testGetAllRepoIssues() {
        Repo repository = new Repo();
        repository.setId(Constants.REPOSITORY_ID_1_UKS_TEST);
        Issue issue1 = new Issue();
        issue1.setRepository(repository);
        Issue issue2 = new Issue();
        issue2.setRepository(repository);

        when(issueRepository.findAllByRepositoryId(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(List.of(issue1, issue2));

        List<IssueDTO> issues = issueService.getAllRepoIssues(Constants.REPOSITORY_ID_1_UKS_TEST);
        assertEquals(issues.size(), 2);
    }

    @Test
    public void testCreateNewIssue() throws NotFoundException {
        // Mocking
        IssueRequest issueRequest = createIssueRequest();
        Issue issue = createIssue();

        when(issueRepository.saveAndFlush(any(Issue.class))).thenReturn(issue);

        // Test
        IssueDTO dto = issueService.createNewIssue(issueRequest);

        // Assertions
        assertNotNull(dto);
        assertEquals("test name3", dto.getName());
        assertEquals(State.OPEN, dto.getState());
        assertEquals(Constants.MIKA_USER_ID, dto.getAuthor().getId());
    }

    @Test
    public void testEditMilestoneOfIssue() throws NotFoundException {
        // Mocking
        Milestone milestone = new Milestone();
        milestone.setId(3L);
        IssueEventRequest eventRequest = createIssueEventRequest(milestone);
        Issue issue = createIssue();
        issue.setMilestone(milestone);
        issue.setId(UUID.randomUUID());

        IssueEvent event = createEvent();
        when(entityManager.getReference(Milestone.class, 3L)).thenReturn(milestone);
        when(issueRepository.saveAndFlush((any(Issue.class)))).thenReturn(issue);
        when(issueRepository.findById(Constants.ISSUE_ID_1)).thenReturn(Optional.of(issue));
        when(issueEventService.createEventFromEventRequest(any(IssueEventRequest.class), any(IssueItemsDTO.class), any(Issue.class))).thenReturn(event);
        // Test
        IssueDTO dto = issueService.updateIssue(eventRequest);

        // Assertions
        assertNotNull(dto);
        assertEquals(dto.getMilestone().getId(), issue.getMilestone().getId());
    }

    @Test
    public void getUserIssues() {

        // Mocking
        UUID id = Constants.PERA_USER_ID;
        Issue issue = new Issue();
        User user = new User();
        user.setId(id);
        issue.setAuthor(user);

        when(issueRepository.findAllByAuthorId(id)).thenReturn(List.of(issue));
        when(issueRepository.findAllByAssigneesId(id)).thenReturn(List.of(issue));
        // Test
        UserIssuesDTO dto = issueService.getUserIssues(id);
        // Assertions
        assertNotNull(dto);
        assertEquals(dto.getAssignedIssues().size(), 1);
        assertEquals(dto.getCreatedIssues().size(), 1);
    }

    private IssueEvent createEvent() {
        IssueEvent event = new IssueEvent();
        User author = new User();
        author.setId(Constants.MIKA_USER_ID);
        event.setAuthor(author);
        event.setNewValue("mile stone");
        return event;
    }

    private IssueEventRequest createIssueEventRequest(Milestone milestone) {
        IssueEventRequest request = new IssueEventRequest();
        request.setIssueId(Constants.ISSUE_ID_1);
        request.setMilestoneId(milestone.getId());
        request.setType(IssueEventType.MILESTONE);
        request.setAuthorId(Constants.MIKA_USER_ID);
        return request;
    }

    private IssueRequest createIssueRequest() {
        IssueRequest issueRequest = new IssueRequest();
        issueRequest.setDescription("test description3");
        issueRequest.setName("test name3");
        issueRequest.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);
        issueRequest.setAuthorId(Constants.MIKA_USER_ID);
        return issueRequest;
    }

    private Issue createIssue() {
        User author = new User();
        author.setId(Constants.MIKA_USER_ID);

        Issue issue = new Issue();
        issue.setDescription("test description3");
        issue.setName("test name3");
        issue.setState(State.OPEN);
        issue.setAuthor(author);
        issue.setEvents(new ArrayList<>());
        return issue;
    }
}
