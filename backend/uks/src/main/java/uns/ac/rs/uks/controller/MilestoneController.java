package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.ChangeStateRequest;
import uns.ac.rs.uks.dto.request.MilestoneRequest;
import uns.ac.rs.uks.dto.response.MilestoneDTO;
import uns.ac.rs.uks.service.MilestoneService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/milestone")
@Slf4j
public class MilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @GetMapping("/getAllRepoMilestones/{repoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<MilestoneDTO> getAllRepoMilestones(@PathVariable UUID repoId) {
        return milestoneService.getAllRepoMilestones(repoId);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public MilestoneDTO createNewMilestone(@Valid @RequestBody MilestoneRequest milestoneRequest) {
        return milestoneService.createNewMilestone(milestoneRequest);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_USER')")
    public MilestoneDTO updateMilestone(@Valid @RequestBody MilestoneRequest milestoneRequest) {
        return milestoneService.editMilestone(milestoneRequest);
    }

    @PutMapping("/changeState")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void changeState(@Valid @RequestBody ChangeStateRequest changeStateRequest) {
        milestoneService.changeState(changeStateRequest);
    }

    @DeleteMapping("/delete/{milestoneId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteLabel(@PathVariable Long milestoneId) {
        milestoneService.deleteMilestone(milestoneId);
    }
}

