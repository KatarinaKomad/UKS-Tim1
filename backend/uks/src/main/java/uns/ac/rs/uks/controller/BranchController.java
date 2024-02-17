package uns.ac.rs.uks.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uns.ac.rs.uks.dto.response.BranchBasicInfoDTO;
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
}
