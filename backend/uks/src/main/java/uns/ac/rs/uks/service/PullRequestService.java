package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.IssueEventRequest;
import uns.ac.rs.uks.dto.request.IssueItem;
import uns.ac.rs.uks.dto.request.IssueRequest;
import uns.ac.rs.uks.dto.request.PullRequestRequest;
import uns.ac.rs.uks.dto.response.PullRequestDTO;
import uns.ac.rs.uks.dto.transport.IssueItemsDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.PullRequestMapper;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.PullRequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PullRequestService {

    @Autowired
    private PullRequestRepository prRepository;

    @Autowired
    private RepoService repoService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private UserService userService;

    @Autowired
    private MilestoneService milestoneService;

    @Autowired
    private PullRequestEventService prEventService;

    @Autowired
    private EntityManager entityManager;

    public PullRequest getById(UUID id) {
        return prRepository.findById(id).orElseThrow(() -> new NotFoundException("Pull request not found."));
    }

    public List<PullRequestDTO> getUserPullRequests(String email, String state, String repoId) {
        List<PullRequest> prs = prRepository.findByAuthorEmail(email);
        if (!state.equalsIgnoreCase("")) {
            prs = prs.stream().filter(pr -> pr.getState().name().equalsIgnoreCase(state)).toList();
        }
        if (!repoId.equalsIgnoreCase("")) {
            prs = prs.stream().filter(pr -> pr.getRepository().getId().toString().equals(repoId)).toList();
        }
        return PullRequestMapper.toDTOs(prs);
    }

    public List<PullRequestDTO> getUserAssigneePullRequests(String email, String state, String repoId) {
        User user = userService.getUserByEmail(email);
        List<PullRequest> prs = prRepository.findAll().stream().filter(pr -> pr.getAssignees().contains(user)).toList();
        if (!state.equalsIgnoreCase("")) {
            prs = prs.stream().filter(pr -> pr.getState().name().equalsIgnoreCase(state)).toList();
        }
        if (!repoId.equalsIgnoreCase("")) {
            prs = prs.stream().filter(pr -> pr.getRepository().getId().toString().equals(repoId)).toList();
        }
        return PullRequestMapper.toDTOs(prs);
    }

    public PullRequestDTO createNewPR(PullRequestRequest dto) {
        Repo repo = repoService.getById(dto.getRepoId());
        List<Label> labels = new ArrayList<>();
        dto.getLabels().forEach(labelId -> labels.add(labelService.getById(labelId)));
        User author = userService.getUserByEmail(dto.getAuthor());
        List<User> assignees = new ArrayList<>();
        dto.getAssignees().forEach(email -> assignees.add(userService.getUserByEmail(email)));
        Milestone milestone = milestoneService.getById(dto.getMilestone());
        PullRequest pullRequest = PullRequestMapper.fromDTO(dto, repo, labels, author, assignees, milestone);
        pullRequest = prRepository.save(pullRequest);
        return PullRequestMapper.toDTO(pullRequest);
    }

    public void deletePullRequest(UUID id) {
        prRepository.deleteById(id);
    }

    public PullRequestDTO changePRState(UUID id, String state) {
        PullRequest pr = getById(id);
        State newState = State.CLOSE.name().equalsIgnoreCase(state) ? State.CLOSE : State.OPEN;
        pr.setState(newState);
        pr = prRepository.save(pr);
        return PullRequestMapper.toDTO(pr);
    }

    public PullRequestDTO getPR(UUID id) {
        PullRequest pr = getById(id);
        PullRequestDTO dto = PullRequestMapper.toDTO(pr);
        dto.setComments(pr.getComments());
        dto.setReviews(pr.getReviews());
        return dto;
    }

    public PullRequestDTO updatePullRequest(IssueEventRequest request) throws NotFoundException {
        PullRequest pr = getById(request.getIssueId());
        // TODO: add necessary participants in issue
        IssueItemsDTO items = getPRItemsByIds(request);
        switch (request.getType()) {
            case LABEL -> pr.setLabels(items.getLabels());
            case ASSIGNEE -> pr.setAssignees(items.getAssignees());
            case MILESTONE -> pr.setMilestone(items.getMilestone());
            case NAME -> pr.setName(request.getName());
            case DESCRIPTION -> pr.setDescription(request.getDescription());
            case STATE -> pr.setState(request.getState());
        }
        PullRequestEvent event =  prEventService.createEventFromEventRequest(request, items, pr);
        pr.getEvents().add(event);
        pr = prRepository.saveAndFlush(pr);
        return PullRequestMapper.toDTO(pr);
    }

    private IssueItemsDTO getPRItemsByIds(IssueItem issueItem) {
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
        if(issueItem.getAssigneeIds() != null && !issueItem.getAssigneeIds().isEmpty()) {
            ArrayList<User> assignees =  new ArrayList<>(issueItem.getAssigneeIds().stream()
                    .map(assigneeId -> entityManager.getReference(User.class, assigneeId)).toList());
            dto.setAssignees(assignees);
        }
        if(issueItem.getLabelIds() != null && !issueItem.getLabelIds().isEmpty()) {
            ArrayList<Label> labels = new ArrayList<>(issueItem.getLabelIds().stream()
                    .map(labelId -> entityManager.getReference(Label.class, labelId)).toList());
            dto.setLabels(labels);
        }
        return dto;
    }
}
