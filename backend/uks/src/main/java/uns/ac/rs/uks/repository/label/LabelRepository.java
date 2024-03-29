package uns.ac.rs.uks.repository.label;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.Label;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long>, CustomLabelRepository {
    List<Label> findAllByRepositoryId(UUID repoId);
}
