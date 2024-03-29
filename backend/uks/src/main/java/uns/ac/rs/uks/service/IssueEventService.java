package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.IssueEventRequest;
import uns.ac.rs.uks.dto.response.IssueEventDTO;
import uns.ac.rs.uks.dto.transport.IssueItemsDTO;
import uns.ac.rs.uks.mapper.IssueEventMapper;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.issue.IssueEventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class IssueEventService {

    @Autowired
    private IssueEventRepository issueEventRepository;
    @Autowired
    private EntityManager entityManager;

    public List<IssueEventDTO> getIssueEventHistory(UUID issueId) {
        List<IssueEvent> events = issueEventRepository.findAllByIssueId(issueId);
        return IssueEventMapper.toDTOs(events);
    }

    public List<IssueEvent> createIssueEventsFromNewIssue(IssueItemsDTO items) {
        List<IssueEvent> events = new ArrayList<>();
        User author = items.getAuthor();
        if(items.getLabels() != null && items.getLabels().size() > 0) {
            String name = String.join(", ", items.getLabels().stream().map(Label::getName).toList());
            events.add(createEvent(name, author, IssueEventType.LABEL));
        }
        if(items.getAssignees() != null && items.getAssignees().size() > 0) {
            String name = String.join(", ", items.getAssignees().stream().map(User::getName).toList());
            events.add(createEvent(name, author, IssueEventType.ASSIGNEE));
        }
        if(items.getMilestone() != null) {
            events.add(createEvent(items.getMilestone().getName(), author, IssueEventType.MILESTONE));
        }
        return events;
    }

    public IssueEvent createEventFromEventRequest(IssueEventRequest request, IssueItemsDTO items, Issue issue) {
        User author = entityManager.getReference(User.class, request.getAuthorId());
        IssueEvent event = new IssueEvent();
        event.setType(request.getType());
        event.setAuthor(author);
        event.setIssue(issue);
        switch (request.getType()) {
            case LABEL -> {
                if(items.getLabels() == null || items.getLabels().size()==0){
                    event.setNewValue("none");
                } else {
                    String name = String.join(", ", items.getLabels().stream().map(Label::getName).toList());
                    event.setNewValue(name);
                }
            }
            case ASSIGNEE -> {
                if(items.getAssignees() == null || items.getAssignees().size()==0){
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

    public void saveAll(List<IssueEvent> events){
        issueEventRepository.saveAll(events);
    }

    private IssueEvent createEvent(String name, User author, IssueEventType type) {
        IssueEvent event = new IssueEvent();
        event.setType(type);
        event.setAuthor(author);
        event.setNewValue(name);
        return event;
    }
}
