package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.IssueEventRequest;
import uns.ac.rs.uks.dto.request.PullRequestRequest;
import uns.ac.rs.uks.dto.response.IssueDTO;
import uns.ac.rs.uks.dto.response.IssueEventDTO;
import uns.ac.rs.uks.dto.response.PullRequestDTO;
import uns.ac.rs.uks.dto.response.PullRequestEventDTO;
import uns.ac.rs.uks.service.PullRequestEventService;
import uns.ac.rs.uks.service.PullRequestService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/pull-request")
@Slf4j
public class PullRequestController {

    @Autowired
    private PullRequestService prService;

    @Autowired
    private PullRequestEventService pullRequestEventService;

    @GetMapping("/getUserPRs/{email}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Map<String, List<PullRequestDTO>> getAllUserPRs(@PathVariable String email, @RequestParam String state, @RequestParam String repoId) {
        List<PullRequestDTO> createdPRs = prService.getUserPullRequests(email, state, repoId);
        List<PullRequestDTO> assignedPRs = prService.getUserAssigneePullRequests(email, state, repoId);
        return Map.of("createdPRs", createdPRs, "assignedPRs", assignedPRs);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public PullRequestDTO createPR(@RequestBody PullRequestRequest pullRequestRequest) {
        return prService.createNewPR(pullRequestRequest);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deletePR(@PathVariable UUID id) {
        prService.deletePullRequest(id);
    }

    @PutMapping("/changeState/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public PullRequestDTO changePRState(@PathVariable UUID id, @RequestBody Map<String, String> state) {
        return prService.changePRState(id, state.get("state"));
    }

    @GetMapping("/getPR/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public PullRequestDTO getPR(@PathVariable UUID id) {
        return prService.getPR(id);
    }

    @GetMapping("/getPREventHistory/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<PullRequestEventDTO> getIssueEventHistory(@PathVariable UUID id) {
        return pullRequestEventService.getPREventHistory(id);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    public PullRequestDTO updateIssue(@RequestBody IssueEventRequest issueEventRequest) {
        return prService.updatePullRequest(issueEventRequest);
    }

}
