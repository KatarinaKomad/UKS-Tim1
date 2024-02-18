package uns.ac.rs.uks.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.UserUpdateRequest;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping(value = "/profile")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getProfileInfo/{userID}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserDTO getProfileInfo(@PathVariable UUID userID){
        return userService.getProfileInfo(userID);
    }

    @PutMapping("/updateMyProfile/{userID}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserDTO updateMyProfile(@RequestBody UserUpdateRequest request, @PathVariable UUID userID) {
        return userService.updateMyProfile(userID, request);
    }

    @DeleteMapping("/delete/{userID}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteMyAccount(@PathVariable UUID userID) {
        userService.deleteUserByUser(userID);
    }
}
