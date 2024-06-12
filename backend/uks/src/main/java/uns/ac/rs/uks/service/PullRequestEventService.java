package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.IssueEventRequest;
import uns.ac.rs.uks.dto.request.PullRequestEventRequest;
import uns.ac.rs.uks.dto.response.PullRequestEventDTO;
import uns.ac.rs.uks.dto.transport.IssueItemsDTO;
import uns.ac.rs.uks.mapper.PullRequestEventMapper;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.PullRequestEventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PullRequestEventService {

    @Autowired
    private PullRequestEventRepository pullRequestEventRepository;
    @Autowired
    private EntityManager entityManager;

    public List<PullRequestEventDTO> getPREventHistory(UUID prId) {
        List<PullRequestEvent> events = pullRequestEventRepository.findAllByPullRequestId(prId);
        return PullRequestEventMapper.toDTOs(events);
    }

    public List<PullRequestEvent> createPREventsFromNewPR(IssueItemsDTO items) {
        List<PullRequestEvent> events = new ArrayList<>();
        User author = items.getAuthor();
        if(items.getLabels() != null && !items.getLabels().isEmpty()) {
            String name = String.join(", ", items.getLabels().stream().map(Label::getName).toList());
            events.add(createEvent(name, author, IssueEventType.LABEL));
        }
        if(items.getAssignees() != null && !items.getAssignees().isEmpty()) {
            String name = String.join(", ", items.getAssignees().stream().map(User::getName).toList());
            events.add(createEvent(name, author, IssueEventType.ASSIGNEE));
        }
        if(items.getMilestone() != null) {
            events.add(createEvent(items.getMilestone().getName(), author, IssueEventType.MILESTONE));
        }
        return events;
    }

    public PullRequestEvent createEventFromEventRequest(PullRequestEventRequest request, IssueItemsDTO items, PullRequest pr) {
        User author = entityManager.getReference(User.class, request.getAuthorId());
        PullRequestEvent event = new PullRequestEvent();
        event.setType(request.getType());
        event.setAuthor(author);
        event.setPullRequest(pr);
        switch (request.getType()) {
            case LABEL -> {
                if(items.getLabels() == null || items.getLabels().isEmpty()){
                    event.setNewValue("none");
                } else {
                    String name = String.join(", ", items.getLabels().stream().map(Label::getName).toList());
                    event.setNewValue(name);
                }
            }
            case ASSIGNEE -> {
                if(items.getAssignees() == null || items.getAssignees().isEmpty()){
                    event.setNewValue("no one");
                } else {
                    String name = String.join(", ", items.getAssignees().stream().map(User::getName).toList());
                    event.setNewValue(name);
                }
            }
            case MILESTONE -> {
                if(items.getMilestone() == null){
                    event.setNewValue("none");
                } else {
                    event.setNewValue(items.getMilestone().getName());
                }

            }
            case NAME -> event.setNewValue(request.getName());
            case DESCRIPTION -> event.setNewValue(request.getDescription());
            case STATE -> event.setNewValue(request.getState().name());
            default -> event.setNewValue("");
        }
        return event;
    }

    public void saveAll(List<PullRequestEvent> events){
        pullRequestEventRepository.saveAll(events);
    }

    private PullRequestEvent createEvent(String name, User author, IssueEventType type) {
        PullRequestEvent event = new PullRequestEvent();
        event.setType(type);
        event.setAuthor(author);
        event.setNewValue(name);
        return event;
    }
}
