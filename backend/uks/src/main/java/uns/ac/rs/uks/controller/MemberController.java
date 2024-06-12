package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.ChangeMemberRoleRequest;
import uns.ac.rs.uks.dto.request.RepoUserRequest;
import uns.ac.rs.uks.dto.response.RepoMemberDTO;
import uns.ac.rs.uks.service.MemberService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/getMembers/{repoId}")
    public List<RepoMemberDTO> getMembers(@PathVariable UUID repoId) {
        return memberService.getMembers(repoId);
    }
    @PostMapping("/inviteUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RepoMemberDTO inviteUser(@Valid @RequestBody RepoUserRequest inviteRequest){
        return memberService.inviteUser(inviteRequest);
    }
    @PostMapping("/acceptInvitation/{link}")
    public RepoMemberDTO acceptInvitation(@PathVariable String link){
        return memberService.acceptInvitation(link);
    }

    @PostMapping("/removeMember")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeMember(@Valid @RequestBody RepoUserRequest inviteRequest){
        memberService.removeMember(inviteRequest);
    }

    @PostMapping("/changeRole")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RepoMemberDTO changeMemberRole(@Valid @RequestBody ChangeMemberRoleRequest request){
        return memberService.changeMemberRole(request);
    }
}
