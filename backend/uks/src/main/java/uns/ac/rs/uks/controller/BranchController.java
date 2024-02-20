package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.MergeBranchesRequest;
import uns.ac.rs.uks.dto.request.ReadCommitsRequest;
import uns.ac.rs.uks.dto.response.BranchBasicInfoDTO;
import uns.ac.rs.uks.dto.response.CommitsResponseDto;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
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
    public List<BranchBasicInfoDTO> getAllByRepoId(@PathVariable UUID repoId) {
        return branchService.getRepoBranches(repoId);
    }

    @GetMapping("/getRepoBranchesFromGitolite/{repoId}")
    public List<BranchBasicInfoDTO> getAllFromGitoliteByRepoId(@PathVariable UUID repoId) {
        return branchService.getGitoliteRepoBranches(repoId);
    }

    @GetMapping("/commits")
    public List<CommitsResponseDto> getBranchCommits(@Valid @RequestBody ReadCommitsRequest request){
        return branchService.getCommits(request.getId(), request.getBranch());
    }

    @GetMapping("/difference/{repoId}")
    public String getBranchDifferences(@PathVariable UUID repoId,
                                        @RequestParam("originBranch") String originBranch,
                                        @RequestParam("destinationBranch") String destinationBranch){
        return branchService.getDifferences(repoId, originBranch, destinationBranch);
    }

    @PutMapping("/mergeBranches")
    public void mergeBranches(@Valid @RequestBody MergeBranchesRequest request){
        branchService.mergeBranches(request.getId(), request.getOriginBranch(), request.getDestinationBranch());
    }

    @DeleteMapping("/{repoId}")
    public void deleteBranch(@PathVariable UUID repoId, @RequestParam String branchName){
        branchService.deleteBranch(repoId, branchName);
    }
}
