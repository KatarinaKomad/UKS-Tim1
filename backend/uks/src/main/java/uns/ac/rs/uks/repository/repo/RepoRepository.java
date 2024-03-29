package uns.ac.rs.uks.repository.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.Repo;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepoRepository extends JpaRepository<Repo, UUID>, CustomRepoRepository {

    List<Repo> findAllByIsPublicTrue();

    List<Repo> findAllByOwnerId(UUID userID);

    List<Repo> findAllByName(String name);
}
