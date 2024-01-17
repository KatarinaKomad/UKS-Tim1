package uns.ac.rs.uks.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
