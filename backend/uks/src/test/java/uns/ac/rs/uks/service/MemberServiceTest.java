package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.request.ChangeMemberRoleRequest;
import uns.ac.rs.uks.dto.request.RepoUserRequest;
import uns.ac.rs.uks.dto.response.RepoMemberDTO;
import uns.ac.rs.uks.model.*;
import uns.ac.rs.uks.repository.MemberRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private EntityManager entityManager;
    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void testFindMemberByUserEmailAndRepositoryIdSuccess() {
        // Mocking
        String email = Constants.MIKA_EMAIL;
        UUID repositoryId = Constants.REPOSITORY_ID_1_UKS_TEST;
        Member memberTest = createMember(email, repositoryId);

        when(memberRepository.findMemberByUserEmailAndRepositoryId(email, repositoryId))
                .thenReturn(Optional.of(memberTest));

        Member member = memberService.findMemberByUserEmailAndRepositoryId(email, repositoryId);
        assertNotNull(member);
        assertEquals(member.getRepository().getId(), repositoryId);
        assertEquals(member.getUser().getEmail(), email);
    }

    @Test
    public void testNoMemberByUserEmailAndRepositoryId() {
        String email = Constants.MIKA_EMAIL;
        UUID repositoryId = Constants.REPOSITORY_ID_1_UKS_TEST;

        when(memberRepository.findMemberByUserEmailAndRepositoryId(email, repositoryId)).thenReturn(Optional.empty());
        Member member = memberService.findMemberByUserEmailAndRepositoryId(email, repositoryId);
        assertNull(member);
    }

    @Test
    void testFindMemberByUserIdAndRepositoryIdSuccess() {
        // Mocking
        UUID userId = Constants.MIKA_USER_ID;
        UUID repositoryId = Constants.REPOSITORY_ID_1_UKS_TEST;
        Member memberTest = createMember(Constants.MIKA_EMAIL, repositoryId);
        memberTest.getUser().setId(userId);

        when(memberRepository.findMemberByUserIdAndRepositoryId(userId, repositoryId))
                .thenReturn(Optional.of(memberTest));

        Member member = memberService.findMemberByUserIdAndRepositoryId(userId, repositoryId);
        assertNotNull(member);
        assertEquals(member.getRepository().getId(), repositoryId);
        assertEquals(member.getUser().getId(), userId);
    }

    @Test
    public void testNoMemberByUserIdAndRepositoryId() {
        UUID userId = Constants.MIKA_USER_ID;
        UUID repositoryId = Constants.REPOSITORY_ID_1_UKS_TEST;

        when(memberRepository.findMemberByUserIdAndRepositoryId(userId, repositoryId)).thenReturn(Optional.empty());
        Member member = memberService.findMemberByUserIdAndRepositoryId(userId, repositoryId);
        assertNull(member);
    }

    @Test
    void testFindMemberByRepositoryId() {
        // Mocking
        UUID userId = Constants.MIKA_USER_ID;
        UUID repositoryId = Constants.REPOSITORY_ID_1_UKS_TEST;
        Member memberTest = createMember(Constants.MIKA_EMAIL, repositoryId);
        memberTest.getUser().setId(userId);

        when(memberRepository.findAllMembersByRepositoryId(repositoryId))
                .thenReturn(List.of(memberTest));

        List<Member> members = memberService.findAllMembersByRepositoryId(repositoryId);
        assertEquals(1, members.size());
        assertEquals(members.get(0).getUser().getId(), userId);
    }

    @Test
    public void testGetMembers() {
        UUID id = Constants.REPOSITORY_ID_1_UKS_TEST;

        Repo repo = new Repo();
        repo.setId(id);
        User user1 = new User();
        user1.setId(Constants.MIKA_USER_ID);
        User user2 = new User();
        user2.setId(Constants.PERA_USER_ID);

        Member member1 = new Member();
        member1.setRepository(repo);
        member1.setUser(user1);
        Member member2 = new Member();
        member2.setRepository(repo);
        member2.setUser(user2);

        when(memberService.findAllMembersByRepositoryId(id)).thenReturn(List.of(member1, member2));

        List<RepoMemberDTO> members = memberService.getMembers(id);
        assertEquals(2, members.size());
    }


//    @Test
//    public void testAcceptInvitation() {
//        String link = "62613664636337392d313434342d343331302d396537642d3937333664656635376636303b306537663261316" +
//                "42d343964302d343463642d386130312d346434303138366636663038";
//
//        User user = new User();
//        user.setId(Constants.MIKA_USER_ID);
//        Repo repo = new Repo();
//        repo.setId(Constants.REPOSITORY_ID_1_UKS_TEST);
//        Member member = new Member();
//        member.setUser(user);
//        member.setRepository(repo);
//        member.setInviteStatus(MemberInviteStatus.PENDING);
//        member.setRepositoryRole(RepositoryRole.COLLABORATOR);
//
//
//        when(memberRepository.findMemberByUserIdAndRepositoryId(Constants.MIKA_USER_ID, Constants.REPOSITORY_ID_1_UKS_TEST))
//                            .thenReturn(Optional.of(member));
//        when(memberRepository.save(any(Member.class))).thenReturn(member);
//
//        RepoMemberDTO memberDTO = memberService.acceptInvitation(link);
//        assertEquals(MemberInviteStatus.ACCEPTED, memberDTO.getInviteStatus());
//    }


    @Test
    public void testInviteUser() {
        RepoUserRequest request = new RepoUserRequest();
        request.setUserId(Constants.MIKA_USER_ID);
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        User user = new User();
        user.setId(Constants.MIKA_USER_ID);

        Repo repo = new Repo();
        repo.setId(Constants.REPOSITORY_ID_1_UKS_TEST);

        Member member = new Member();
        member.setUser(user);
        member.setRepository(repo);
        member.setInviteStatus(MemberInviteStatus.PENDING);
        member.setRepositoryRole(RepositoryRole.COLLABORATOR);


        when(entityManager.getReference(Repo.class, Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(repo);
        when(entityManager.getReference(User.class, Constants.MIKA_USER_ID)).thenReturn(user);
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        doNothing().when(emailService).sendInviteUserEmail(any(Repo.class), any(User.class), any(String.class));

        RepoMemberDTO memberDTO = memberService.inviteUser(request);
        assertEquals(MemberInviteStatus.PENDING, memberDTO.getInviteStatus());
    }

    @Test
    public void testRemoveUser() {
        RepoUserRequest request = new RepoUserRequest();
        request.setUserId(Constants.MIKA_USER_ID);
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);

        Member memberTest = createMember(Constants.MIKA_EMAIL, Constants.REPOSITORY_ID_1_UKS_TEST);
        memberTest.getUser().setId(Constants.MIKA_USER_ID);

        when(memberRepository.findMemberByUserIdAndRepositoryId(Constants.MIKA_USER_ID, Constants.REPOSITORY_ID_1_UKS_TEST))
                .thenReturn(Optional.of(memberTest));
        doNothing().when(memberRepository).delete(memberTest);
    }

    @Test
    public void testChangeMemberRole() {
        ChangeMemberRoleRequest request = new ChangeMemberRoleRequest();
        request.setUserId(Constants.PERA_USER_ID);
        request.setRepoId(Constants.REPOSITORY_ID_1_UKS_TEST);
        request.setRepositoryRole(RepositoryRole.VIEWER);

        User user = new User();
        user.setId(Constants.PERA_USER_ID);

        Repo repo = new Repo();
        repo.setId(Constants.REPOSITORY_ID_1_UKS_TEST);

        Member member = new Member();
        member.setUser(user);
        member.setRepository(repo);
        member.setInviteStatus(MemberInviteStatus.ACCEPTED);
        member.setRepositoryRole(RepositoryRole.VIEWER);

        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(memberRepository.findMemberByUserIdAndRepositoryId(Constants.PERA_USER_ID, Constants.REPOSITORY_ID_1_UKS_TEST))
                .thenReturn(Optional.of(member));

        RepoMemberDTO memberDTO = memberService.changeMemberRole(request);
        assertEquals(RepositoryRole.VIEWER, memberDTO.getRepositoryRole());
    }

    private Member createMember(String email, UUID repoId) {
        Repo repository = new Repo();
        repository.setId(repoId);

        User user = new User();
        user.setEmail(email);

        Member member = new Member();
        member.setUser(user);
        member.setRepository(repository);

        return member;
    }
}
