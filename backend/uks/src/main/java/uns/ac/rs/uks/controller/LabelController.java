package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.LabelRequest;
import uns.ac.rs.uks.dto.response.LabelDTO;
import uns.ac.rs.uks.service.LabelService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/label")
@Slf4j
public class LabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping("/getAllRepoLabels/{repoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<LabelDTO> getAllRepoLabels(@PathVariable UUID repoId) {
        return labelService.getAllRepoLabels(repoId);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    public LabelDTO createNewLabel(@Valid @RequestBody LabelRequest labelRequest) {
        return labelService.createNewLabel(labelRequest);
    }

    @PatchMapping("/edit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public LabelDTO editLabel(@Valid @RequestBody LabelRequest labelRequest) {
        return labelService.editLabel(labelRequest);
    }

    @DeleteMapping("/delete/{labelId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteLabel(@PathVariable Long labelId) {
        labelService.deleteLabel(labelId);
    }
}
