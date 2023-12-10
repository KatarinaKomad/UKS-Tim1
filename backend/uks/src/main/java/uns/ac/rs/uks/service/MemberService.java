package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.model.Member;
import uns.ac.rs.uks.repository.MemberRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Optional<Member> findMemberByUserEmailAndRepositoryId(String email, UUID repositoryId){
        return memberRepository.findMemberByUserEmailAndRepositoryId(email, repositoryId);
    }

}
