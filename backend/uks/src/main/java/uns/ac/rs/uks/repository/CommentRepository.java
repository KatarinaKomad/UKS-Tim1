package uns.ac.rs.uks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.Comment;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemId(UUID itemId);
}
