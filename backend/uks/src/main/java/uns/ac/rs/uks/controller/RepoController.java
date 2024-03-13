package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.*;
import uns.ac.rs.uks.dto.response.*;
import uns.ac.rs.uks.model.Branch;
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

    @PutMapping("/update/{repositoryId}")
    @PreAuthorize("hasPermission(#repositoryId, 'OWNER')")
    public RepoBasicInfoDTO update(@RequestBody RepoUpdateRequest request, @PathVariable UUID repositoryId) {
        return repoService.updateRepo(repositoryId, request);
    }

    @GetMapping("/getById/{repoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RepoBasicInfoDTO getById(@PathVariable UUID repoId) {
        return repoService.findById(repoId);
    }

    @PostMapping("/fork")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RepoBasicInfoDTO forkRepo(@Valid @RequestBody RepoForkRequest forkRequest) {
        return repoService.forkRepo(forkRequest);
    }

    @GetMapping("/getAllForked/{repoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<RepoBasicInfoDTO> getAllForked(@PathVariable UUID repoId) {
        return repoService.getAllForked(repoId);
    }

    @PostMapping("/star")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RepoBasicInfoDTO starRepo(@Valid @RequestBody RepoUserRequest starRequest) {
        return repoService.starRepo(starRequest);
    }

    @GetMapping("/getAllStargazers/{repoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<UserDTO> getAllStargazers(@PathVariable UUID repoId) {
        return repoService.getAllStargazers(repoId);
    }

    @PostMapping("/watch")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RepoBasicInfoDTO watchRepo(@Valid @RequestBody RepoUserRequest watchRequest) {
        return repoService.watchRepo(watchRequest);
    }

    @GetMapping("/getAllWatchers/{repoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<UserDTO> getAllWatchers(@PathVariable UUID repoId) {
        return repoService.getAllWatchers(repoId);
    }

    @PostMapping("/amIWatchingStargazing")
    @PreAuthorize("hasRole('ROLE_USER')")
    public WatchStarResponseDTO amIWatchingStargazing(@Valid @RequestBody RepoUserRequest watchRequest) {
        return repoService.amIWatchingStargazing(watchRequest);
    }

    @DeleteMapping("/delete/{repoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteRepo(@PathVariable UUID repoId) {
        repoService.deleteRepo(repoId);
    }

    @GetMapping("/getDefaultBranch/{repoId}")
    public BranchDTO getDefaultBranch(@PathVariable UUID repoId) {
        return repoService.getDefaultBranch(repoId);
    }

    @PostMapping("/getFiles")
    public List<FileDTO> getFiles(@Valid @RequestBody FileRequest request) {
        return repoService.getFiles(request);
    }
}
