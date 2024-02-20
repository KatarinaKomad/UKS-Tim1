package uns.ac.rs.uks.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.IssueEventRequest;
import uns.ac.rs.uks.dto.request.IssueRequest;
import uns.ac.rs.uks.dto.response.IssueDTO;
import uns.ac.rs.uks.dto.response.IssueEventDTO;
import uns.ac.rs.uks.dto.response.UserIssuesDTO;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.security.TokenBasedAuthentication;
import uns.ac.rs.uks.service.IssueEventService;
import uns.ac.rs.uks.service.IssueService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/issue")
@Slf4j
public class IssueController {

    @Autowired
    private IssueService issueService;
    @Autowired
    private IssueEventService issueEventService;

    @GetMapping("/getAllRepoIssues/{repoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<IssueDTO> getAllRepoIssues(@PathVariable UUID repoId) {
        return issueService.getAllRepoIssues(repoId);
    }

    @GetMapping("/getIssueEventHistory/{issueId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<IssueEventDTO> getIssueEventHistory(@PathVariable UUID issueId) {
        return issueEventService.getIssueEventHistory(issueId);
    }

    @GetMapping("/getById/{issueId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public IssueDTO getIssueById(@PathVariable UUID issueId) {
        return issueService.findById(issueId);
    }

    @GetMapping("/getMyIssues/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserIssuesDTO getIssueByUserId(@PathVariable UUID userId) {
        return issueService.getUserIssues(userId);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public IssueDTO createNewIssue(@Valid @RequestBody IssueRequest issueRequest) {
        return issueService.createNewIssue(issueRequest);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    public IssueDTO updateIssue(@Valid @RequestBody IssueEventRequest issueEventRequest) {
        return issueService.updateIssue(issueEventRequest);
    }

    @DeleteMapping("/delete/{issueId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteIssue(@PathVariable UUID issueId) {
        issueService.deleteIssue(issueId);
    }
}
