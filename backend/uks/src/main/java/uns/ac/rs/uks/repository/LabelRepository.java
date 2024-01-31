package uns.ac.rs.uks.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.Label;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long>, CustomLabelRepository {
    List<Label> findAllByRepositoryId(UUID repoId);
}
