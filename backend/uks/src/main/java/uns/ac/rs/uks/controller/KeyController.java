package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uns.ac.rs.uks.dto.request.KeyRequest;
import uns.ac.rs.uks.dto.response.KeyResponse;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.service.GitoliteService;

@RestController
@RequestMapping(value = "/key")
@Slf4j
public class KeyController {

    @Autowired
    private GitoliteService gitoliteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public KeyResponse createKey(@Valid @RequestBody KeyRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return gitoliteService.createKey(request.getValue(), user.getUsername());
    }
}
