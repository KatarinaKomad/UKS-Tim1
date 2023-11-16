package uns.ac.rs.uks.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import uns.ac.rs.uks.dto.request.LoginRequest;
import uns.ac.rs.uks.dto.response.TokenResponse;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.service.AuthService;

@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request) {
        try {
            TokenResponse token = authService.login(loginRequest);
            return ResponseEntity.ok(token);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/testAdmin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> testAdmin( HttpServletResponse response, HttpServletRequest request) {
        log.info("testAdmin success");
        return ResponseEntity.ok().build();
    }
    @GetMapping("/testOwner")
    @PreAuthorize("hasAuthority('ROLE_OWNER')")
    public ResponseEntity<?> testOwner(HttpServletResponse response, HttpServletRequest request) {
        log.info("testOwner success");
        return ResponseEntity.ok().build();
    }
    @GetMapping("/testCollaborator")
    @PreAuthorize("hasRole('ROLE_COLLABORATOR')")
    public ResponseEntity<?> testCollaborator(HttpServletResponse response, HttpServletRequest request) {
        log.info("testCollaborator success");
        return ResponseEntity.ok().build();
    }
}
