package uns.ac.rs.uks.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.Member;
import uns.ac.rs.uks.util.Constants;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DataJpaTest
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @ParameterizedTest(name = "Finding repository member by email {0} and repository id {1}")
    @MethodSource("memberExistsProvider")
    public void memberByEmailAndRepositoryIdExists(String email, UUID repositoryId) {
        Optional<Member> optionalMember = memberRepository.findMemberByUserEmailAndRepositoryId(email, repositoryId);
        assertTrue(optionalMember.isPresent());
        Member member = optionalMember.get();
        assertEquals(email, member.getUser().getEmail());
        assertEquals(repositoryId, member.getRepository().getId());
    }

    @ParameterizedTest(name = "Not finding repository member by email {0} and repository id {1}")
    @MethodSource("memberDoNotExistProvider")
    public void noMembersByEmailAndRepositoryId(String email, UUID repositoryId) {
        Optional<Member> optionalMember = memberRepository.findMemberByUserEmailAndRepositoryId(email, repositoryId);
        assertTrue(optionalMember.isEmpty());
    }

    static List<Arguments> memberExistsProvider() {
        return List.of(
                arguments("mika@gmail.com", Constants.REPOSITORY_ID_1_UKS_TEST),
                arguments("pera@gmail.com", Constants.REPOSITORY_ID_1_UKS_TEST)
        );
    }

    static List<Arguments> memberDoNotExistProvider() {
        return List.of(
                arguments("admin@gmail.com", Constants.REPOSITORY_ID_1_UKS_TEST)
        );
    }
}
