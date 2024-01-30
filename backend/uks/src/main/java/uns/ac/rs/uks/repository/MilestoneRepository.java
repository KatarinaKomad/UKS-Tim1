package uns.ac.rs.uks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.Milestone;
import uns.ac.rs.uks.model.State;

import java.util.List;
import java.util.UUID;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findAllByRepositoryId(UUID repoId);

    @Modifying
    @Query("UPDATE Milestone m SET m.state = ?2 WHERE m.id = ?1")
    void updateState(Long id, State state);
}
