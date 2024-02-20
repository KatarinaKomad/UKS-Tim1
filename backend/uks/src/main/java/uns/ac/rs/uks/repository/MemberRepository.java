package uns.ac.rs.uks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByUserEmailAndRepositoryId(String email, UUID repositoryId);
    Optional<Member> findMemberByUserIdAndRepositoryId(UUID userId, UUID repositoryId);

    List<Member> findAllMembersByRepositoryId(UUID repositoryId);
}
