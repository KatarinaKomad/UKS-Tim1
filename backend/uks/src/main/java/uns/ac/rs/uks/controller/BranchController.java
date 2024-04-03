package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.CommitDiffRequest;
import uns.ac.rs.uks.dto.request.OriginTargetBranchRequest;
import uns.ac.rs.uks.dto.request.TargetBranchRequest;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.dto.response.CommitDiffResponseDTO;
import uns.ac.rs.uks.dto.response.CommitsResponseDto;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.service.BranchService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/branch")
@Slf4j
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping("/getRepoBranches/{repoId}")
    public List<BranchDTO> getAllByRepoId(@PathVariable UUID repoId) {
        return branchService.getRepoBranches(repoId);
    }

    @GetMapping("/getRepoBranchesCount/{repoId}")
    public Long getRepoBranchesCount(@PathVariable UUID repoId) {
        return branchService.getRepoBranchesCount(repoId);
    }

    @PostMapping("/commitDiff")
    public CommitDiffResponseDTO getCommitDiff(@Valid @RequestBody CommitDiffRequest request){
        return branchService.getCommitDiff(request.getRepoId(), request.getBranchName(), request.getCommit());
    }

    @PutMapping("/mergeBranches")
    public void mergeBranches(@Valid @RequestBody OriginTargetBranchRequest request, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        branchService.mergeBranches(request, user);
    }

    @PostMapping("/delete")
    public void deleteBranch(@Valid @RequestBody TargetBranchRequest request){
        branchService.deleteBranch(request.getRepoId(), request.getBranchName());
    }

    @PostMapping("/newBranch")
    public BranchDTO newBranch(@Valid @RequestBody OriginTargetBranchRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return branchService.newBranch(request, user);
    }

    @PostMapping("/renameBranch")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BranchDTO renameBranch(@Valid @RequestBody OriginTargetBranchRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return branchService.renameBranch(request, user);
    }
}
