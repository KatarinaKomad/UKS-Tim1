package uns.ac.rs.uks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.IssueEvent;
import uns.ac.rs.uks.model.PullRequestEvent;

import java.util.List;
import java.util.UUID;

@Repository
public interface PullRequestEventRepository extends JpaRepository<PullRequestEvent, Long> {

    List<PullRequestEvent> findAllByPullRequestId(UUID issueId);
}
