package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.IssueRequest;
import uns.ac.rs.uks.dto.response.IssueDTO;
import uns.ac.rs.uks.dto.transport.IssueItemsDTO;
import uns.ac.rs.uks.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IssueMapper {

    public static IssueDTO toDTO(Issue issue) {
        return IssueDTO.builder()
                .id(issue.getId())
                .counter(issue.getCounter())
                .name(issue.getName())
                .description(issue.getDescription())
                .state(issue.getState())
                .author(issue.getAuthor() != null ? UserMapper.toDTO(issue.getAuthor()) : null)
                .createdAt(issue.getCreatedAt())
                .assignees(issue.getAssignees() != null ? UserMapper.toDTOs(issue.getAssignees()) : new ArrayList<>())
                .milestone(issue.getMilestone() != null ? MilestoneMapper.toDTO(issue.getMilestone()) : null)
                .labels(issue.getLabels() != null ? LabelMapper.toDTOs(issue.getLabels()) : new ArrayList<>())
                .participants(issue.getParticipants() != null ? UserMapper.toDTOs(issue.getParticipants()) : new ArrayList<>())
                .build();
    }

    public static List<IssueDTO> toDTOs(List<Issue> issues) {
        return issues.stream().map(IssueMapper::toDTO).toList();
    }

    public static Issue newIssueFromDTO(IssueRequest issueRequest, IssueItemsDTO items) {
        ArrayList<User> participants = new ArrayList<>();
        participants.add(items.getAuthor());
        if(items.getAssignees() != null){
            participants.addAll(items.getAssignees());
        }

        Issue issue = new Issue();
        issue.setId(UUID.randomUUID());

        issue.setName(issueRequest.getName());
        issue.setDescription(issueRequest.getDescription());
        issue.setState(State.OPEN);

        issue.setAuthor(items.getAuthor());
        issue.setAssignees(items.getAssignees());
        issue.setMilestone(items.getMilestone());
        issue.setLabels(items.getLabels());
        issue.setRepository(items.getRepo());
        issue.setParticipants(participants);

        return issue;
    }



}
