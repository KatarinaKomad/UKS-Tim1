package uns.ac.rs.uks.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import uns.ac.rs.uks.dto.request.LoginRequest;
import uns.ac.rs.uks.dto.request.RegistrationRequest;
import uns.ac.rs.uks.dto.response.TokenResponse;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.UserMapper;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.service.AccountService;
import uns.ac.rs.uks.service.AuthService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            TokenResponse token = authService.login(loginRequest);
            return ResponseEntity.ok(token);
        } catch (NotFoundException | InternalAuthenticationServiceException | BadCredentialsException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/register")
    public UserDTO register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        return accountService.registerUser(registrationRequest);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public UserDTO me(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return UserMapper.toDTO(user);
    }


    @GetMapping("/testAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> testAdmin(HttpServletRequest request) {
        log.info("testAdmin success");
        return ResponseEntity.ok().build();
    }
    @GetMapping("/testUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> testUser(HttpServletRequest request) {
        log.info("testAdmin success");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/testOwner/{repositoryId}")
    @PreAuthorize("hasPermission(#repositoryId, 'OWNER')")
    public ResponseEntity<?> testOwner(HttpServletRequest request, @PathVariable UUID repositoryId ) {
        log.info("testOwner success");
        return ResponseEntity.ok().build();
    }
    @GetMapping("/testCollaborator/{repositoryId}")
    @PreAuthorize("hasPermission(#repositoryId, 'COLLABORATOR')")
    public ResponseEntity<?> testCollaborator(HttpServletRequest request, @PathVariable UUID repositoryId ) {
        log.info("testCollaborator success");
        return ResponseEntity.ok().build();
    }
}
