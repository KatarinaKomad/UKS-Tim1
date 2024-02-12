package uns.ac.rs.uks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.Commit;

import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {
    List<Commit> findAllByBranchId(Long branchId);
}
