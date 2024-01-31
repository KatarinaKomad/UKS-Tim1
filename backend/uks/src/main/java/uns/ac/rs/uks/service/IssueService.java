package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.IssueEventRequest;
import uns.ac.rs.uks.dto.request.IssueItem;
import uns.ac.rs.uks.dto.request.IssueRequest;
import uns.ac.rs.uks.dto.response.IssueDTO;
import uns.ac.rs.uks.dto.transport.IssueItemsDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.IssueMapper;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.IssueRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private IssueEventService issueEventService;
    @Autowired
    private EntityManager entityManager;

    public Issue getById(UUID issueId) {
        return issueRepository.findById(issueId).orElseThrow(()->new NotFoundException("Issue not found."));
    }

    public List<IssueDTO> getAllRepoIssues(UUID repoId) {
        List<Issue> issues = issueRepository.findAllByRepositoryId(repoId);
        return IssueMapper.toDTOs(issues);
    }

    public IssueDTO createNewIssue(IssueRequest issueRequest) throws NotFoundException {
        IssueItemsDTO items = getIssueItemsByIds(issueRequest);
        Issue issue = IssueMapper.newIssueFromDTO(issueRequest, items);
        List<IssueEvent> events = issueEventService.createIssueEventsFromNewIssue(items);
        issue.setEvents(events);
        issue = issueRepository.save(issue);
        return IssueMapper.toDTO(issue);
    }

    public IssueDTO updateIssue(IssueEventRequest request) throws NotFoundException {
        Issue issue = getById(request.getIssueId());
        // TODO: add necessary participants in issue
        IssueItemsDTO items = getIssueItemsByIds(request);
        switch (request.getType()) {
            case LABEL -> issue.setLabels(items.getLabels());
            case ASSIGNEE -> issue.setAssignees(items.getAssignees());
            case MILESTONE -> issue.setMilestone(items.getMilestone());
            case NAME -> issue.setName(request.getName());
            case DESCRIPTION -> issue.setDescription(request.getDescription());
            case STATE -> issue.setState(request.getState());
        }
        IssueEvent event =  issueEventService.createEventFromEventRequest(request, items, issue);
        issue.getEvents().add(event);
        issue = issueRepository.save(issue);
        return IssueMapper.toDTO(issue);
    }

    public void deleteIssue(UUID issueId) {
        issueRepository.deleteById(issueId);
    }

    private IssueItemsDTO getIssueItemsByIds(IssueItem issueItem) {
        IssueItemsDTO dto = new IssueItemsDTO();
        if(issueItem instanceof IssueRequest request){
            dto.setRepo(entityManager.getReference(Repo.class, request.getRepoId()));
        }
        if(issueItem.getAuthorId() != null) {
           dto.setAuthor(entityManager.getReference(User.class, issueItem.getAuthorId()));
        }
        if(issueItem.getMilestoneId() != null) {
            dto.setMilestone(entityManager.getReference(Milestone.class, issueItem.getMilestoneId()));
        }
        if(issueItem.getAssigneeIds() != null && issueItem.getAssigneeIds().size()>0) {
            ArrayList<User> assignees =  new ArrayList<>(issueItem.getAssigneeIds().stream()
                    .map(assigneeId -> entityManager.getReference(User.class, assigneeId)).toList());
            dto.setAssignees(assignees);
        }
        if(issueItem.getLabelIds() != null && issueItem.getLabelIds().size()>0) {
            ArrayList<Label> labels = new ArrayList<>(issueItem.getLabelIds().stream()
                    .map(labelId -> entityManager.getReference(Label.class, labelId)).toList());
            dto.setLabels(labels);
        }
        return dto;
    }
}
