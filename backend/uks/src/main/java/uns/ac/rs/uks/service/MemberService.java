package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.ChangeMemberRoleRequest;
import uns.ac.rs.uks.dto.request.RepoUserRequest;
import uns.ac.rs.uks.dto.response.RepoMemberDTO;
import uns.ac.rs.uks.mapper.MemberMapper;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.MemberRepository;
import uns.ac.rs.uks.util.EncryptionUtil;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmailService emailService;
    @Value("${application.frontendLink}")
    private String frontendLink;

    public Member findMemberByUserEmailAndRepositoryId(String email, UUID repositoryId){
        return memberRepository.findMemberByUserEmailAndRepositoryId(email, repositoryId).orElse(null);
    }
    public Member findMemberByUserIdAndRepositoryId(UUID userId, UUID repositoryId){
        return memberRepository.findMemberByUserIdAndRepositoryId(userId, repositoryId).orElse(null);
    }

    public List<Member> findAllMembersByRepositoryId(UUID repositoryId){
        return memberRepository.findAllMembersByRepositoryId(repositoryId);
    }

    public List<RepoMemberDTO> getMembers(UUID repoId) {
        List<Member> members = findAllMembersByRepositoryId(repoId);
        return MemberMapper.toRepoMemberDTOs(members);
    }

    public Member addNewMember(User user, Repo repo, RepositoryRole role, MemberInviteStatus inviteStatus) {
        Member member = new Member();
        member.setUser(user);
        member.setRepository(repo);
        member.setRepositoryRole(role);
        member.setInviteStatus(inviteStatus);
        return memberRepository.save(member);
    }

    public RepoMemberDTO inviteUser(RepoUserRequest inviteRequest) {
        Repo repo = entityManager.getReference(Repo.class, inviteRequest.getRepoId());
        User user = entityManager.getReference(User.class, inviteRequest.getUserId());

        String encryptedStr = EncryptionUtil.encodeHex(inviteRequest.getRepoId() + ";" + inviteRequest.getUserId());
        String link = frontendLink + "invite-verification/" + encryptedStr + "?repoName=" +  repo.getName();

        emailService.sendInviteUserEmail(repo, user, link);
        Member member = addNewMember(user, repo, RepositoryRole.COLLABORATOR, MemberInviteStatus.PENDING);
        return MemberMapper.toRepoMemberDTO(member);
    }

    public RepoMemberDTO acceptInvitation(String encodedLink) {
        String[] parts = EncryptionUtil.decodeHex(encodedLink).split(";");

        Member member = findMemberByUserIdAndRepositoryId(UUID.fromString(parts[1]), UUID.fromString(parts[0]));
        member.setInviteStatus(MemberInviteStatus.ACCEPTED);
        return MemberMapper.toRepoMemberDTO(memberRepository.save(member));
    }

    public void removeMember(RepoUserRequest request) {
        Member member = findMemberByUserIdAndRepositoryId(request.getUserId(), request.getRepoId());
        memberRepository.delete(member);
    }

    public RepoMemberDTO changeMemberRole(ChangeMemberRoleRequest request) {
        Member member = findMemberByUserIdAndRepositoryId(request.getUserId(), request.getRepoId());
        member.setRepositoryRole(request.getRepositoryRole());
        return MemberMapper.toRepoMemberDTO(memberRepository.save(member));
    }

    public void removeAllMembersFromRepo(UUID repoId) {
        List<RepoMemberDTO> members = getMembers(repoId);
        for (RepoMemberDTO member: members) {
            removeMember(new RepoUserRequest(member.getId(), member.getRepoId()));
        }
    }
}
