package uns.ac.rs.uks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.PullRequest;

import java.util.List;
import java.util.UUID;

@Repository
public interface PullRequestRepository extends JpaRepository<PullRequest, UUID> {

    @Query("SELECT pr FROM PullRequest pr WHERE pr.author.email = ?1")
    List<PullRequest> findByAuthorEmail(String email);
}
