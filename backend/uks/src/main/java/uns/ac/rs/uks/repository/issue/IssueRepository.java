package uns.ac.rs.uks.repository.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.Issue;

import java.util.List;
import java.util.UUID;

@Repository
public interface IssueRepository extends JpaRepository<Issue, UUID>, CustomIssueRepository {
    List<Issue> findAllByRepositoryId(UUID repoId);

    List<Issue> findAllByAuthorId(UUID id);

    List<Issue> findAllByAssigneesId(UUID id);
}
