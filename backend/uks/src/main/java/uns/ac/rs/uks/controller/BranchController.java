package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.BranchRequest;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.service.BranchService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/branch")
@Slf4j
public class BranchController {
    @Autowired
    private BranchService branchService;

    @GetMapping("/getAllRepoBranches/{repoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BranchDTO> getAllRepoBranches(@PathVariable UUID repoId) {
        return branchService.getAllRepoBranches(repoId);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BranchDTO createNewBranch(@Valid @RequestBody BranchRequest branchRequest) {
        return branchService.createNewBranch(branchRequest);
    }

    @PutMapping("/editName")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BranchDTO editBranchName(@Valid @RequestBody BranchRequest branchRequest) {
        return branchService.editBranchName(branchRequest);
    }

    @DeleteMapping("/delete/{branchId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteBranch(@PathVariable Long branchId) {
        branchService.deleteBranch(branchId);
    }
}
