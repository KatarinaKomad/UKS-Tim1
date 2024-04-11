package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.PullRequestRequest;
import uns.ac.rs.uks.dto.response.PullRequestDTO;
import uns.ac.rs.uks.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PullRequestMapper {

    public static PullRequestDTO toDTO(PullRequest pr) {
        return PullRequestDTO.builder()
                .id(pr.getId())
                .name(pr.getName())
                .author(pr.getAuthor() != null ? UserMapper.toDTO(pr.getAuthor()) : null)
                .counter(pr.getCounter())
                .createdAt(pr.getCreatedAt())
                .assignees(pr.getAssignees() != null ? UserMapper.toDTOs(pr.getAssignees()) : new ArrayList<>())
                .labels(pr.getLabels() != null ? LabelMapper.toDTOs(pr.getLabels()) : new ArrayList<>())
                .state(pr.getState())
                .milestone(pr.getMilestone() != null ? MilestoneMapper.toDTO(pr.getMilestone()) : null)
                .description(pr.getDescription())
                .origin(pr.getOrigin())
                .target(pr.getTarget())
                .repo(pr.getRepository() != null ? RepoMapper.toDTO(pr.getRepository()) : null)
                .build();
    }

    public static List<PullRequestDTO> toDTOs(List<PullRequest> pullRequests) {
        return pullRequests.stream().map(PullRequestMapper::toDTO).toList();
    }

    public static PullRequest fromDTO(PullRequestRequest pr, Repo repo, List<Label> labels, User author, List<User> assignees, Milestone milestone) {
        PullRequest pullRequest = new PullRequest();
        pullRequest.setId(UUID.randomUUID());
        pullRequest.setName(pr.getName());
        pullRequest.setDescription(pr.getDescription());
        pullRequest.setRepository(repo);
        pullRequest.setLabels(labels);
        pullRequest.setAuthor(author);
        pullRequest.setAssignees(assignees);
        pullRequest.setMilestone(milestone);
        pullRequest.setState(State.OPEN);
        pullRequest.setOrigin(pr.getOrigin());
        pullRequest.setTarget(pr.getTarget());
        return pullRequest;
    }
}
