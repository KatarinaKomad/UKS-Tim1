package uns.ac.rs.uks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uns.ac.rs.uks.model.Branch;
import uns.ac.rs.uks.model.Repo;

import java.util.List;
import java.util.UUID;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findAllByRepositoryId(UUID repositoryId);
    @Query("SELECT COUNT(b) FROM Branch b WHERE b.repository.id = ?1")
    Long countAllByRepositoryId(UUID repositoryId);

    Branch findByRepositoryIdAndName(UUID repoId, String branchName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Branch b WHERE b.repository.id = ?1 AND b.name = ?2")
    void deleteByRepositoryIdAndName(UUID repoId, String branchName);
}
