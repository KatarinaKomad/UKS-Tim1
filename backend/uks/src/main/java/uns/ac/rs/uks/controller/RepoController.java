package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
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
    public List<RepoBasicInfoDTO> getMyRepos(@PathVariable UUID userID ) {
        return repoService.getMyRepos(userID);
    }

    @PostMapping("/create")
    public RepoBasicInfoDTO createNewRepo(@Valid @RequestBody RepoRequest repoRequest) {
        return repoService.createNewRepo(repoRequest);
    }
}
