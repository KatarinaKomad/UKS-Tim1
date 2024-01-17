package uns.ac.rs.uks.service;

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
import uns.ac.rs.uks.model.Member;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.MemberRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
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
