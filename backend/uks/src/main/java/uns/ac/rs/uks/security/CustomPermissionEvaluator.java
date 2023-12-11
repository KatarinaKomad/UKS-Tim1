package uns.ac.rs.uks.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import uns.ac.rs.uks.model.Member;
import uns.ac.rs.uks.model.RepositoryRole;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.service.MemberService;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final MemberService memberService;
    public CustomPermissionEvaluator(MemberService memberService){
        this.memberService = memberService;
    }


    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        User user = (User) auth.getPrincipal();
        UUID repositoryId =  (UUID) targetDomainObject;
        RepositoryRole repositoryRole = RepositoryRole.valueOf((String) permission);
        Optional<Member> member = memberService.findMemberByUserEmailAndRepositoryId(user.getUsername(), repositoryId);
        return member.map(value -> value.getRepositoryRole().equals(repositoryRole)).orElse(false);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}