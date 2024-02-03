package uns.ac.rs.uks.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.EditRepoRequest;
import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.request.RepoUpdateRequest;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.service.RepoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/repo")
@Slf4j
public class RepoController {

    @Autowired
    private RepoService repoService;

    @GetMapping("/getAllPublic")
    public List<RepoBasicInfoDTO> getAllPublic() {
        return repoService.getAllPublic();
    }

    @GetMapping("/getMyRepos/{userID}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<RepoBasicInfoDTO> getMyRepos(@PathVariable UUID userID) {
        return repoService.getMyRepos(userID);
    }

    @PostMapping("/create")
    public RepoBasicInfoDTO createNewRepo(@Valid @RequestBody RepoRequest repoRequest) {
        return repoService.createNewRepo(repoRequest);
    }

    @PostMapping("/validateOverviewByRepoName")
    public RepoBasicInfoDTO validateOverviewByRepoName(@Valid @RequestBody RepoRequest repoRequest) {
        return repoService.getByNameAndPublicOrMember(repoRequest);
    }

    @PostMapping("/canEditRepoItems")
    public Boolean canEditRepoItems(@Valid @RequestBody EditRepoRequest repoRequest) {
        return repoService.canEditRepoItems(repoRequest);
    }

    @GetMapping("/getMembers/{repoId}")
    // @PreAuthorize("hasPermission(#repositoryId, 'OWNER')")
    public List<UserDTO> getMembers(@PathVariable UUID repoId) {
        return repoService.getMembers(repoId);
    }

    @PutMapping("/update/{repositoryId}")
    @PreAuthorize("hasPermission(#repositoryId, 'OWNER')")
    public RepoBasicInfoDTO update(@RequestBody RepoUpdateRequest request, @PathVariable UUID repositoryId) {
        return repoService.updateRepo(repositoryId, request);
    }
}
