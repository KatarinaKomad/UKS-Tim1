package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.model.Member;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.RepositoryRole;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.MemberRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member findMemberByUserEmailAndRepositoryId(String email, UUID repositoryId){
        return memberRepository.findMemberByUserEmailAndRepositoryId(email, repositoryId).orElse(null);
    }
    public Member findMemberByUserIdAndRepositoryId(UUID userId, UUID repositoryId){
        return memberRepository.findMemberByUserIdAndRepositoryId(userId, repositoryId).orElse(null);
    }

    public List<Member> findAllMembersByRepositoryId(UUID repositoryId){
        return memberRepository.findAllMembersByRepositoryId(repositoryId);
    }

    public void addNewMember(User user, Repo repo, RepositoryRole role) {
        Member member = new Member();
        member.setUser(user);
        member.setRepository(repo);
        member.setRepositoryRole(role);
        memberRepository.save(member);
    }
}
