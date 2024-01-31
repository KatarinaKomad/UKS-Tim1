package uns.ac.rs.uks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.IssueEvent;

import java.util.List;
import java.util.UUID;

@Repository
public interface IssueEventRepository extends JpaRepository<IssueEvent, Long> {

    List<IssueEvent> findAllByIssueId(UUID issueId);
}
