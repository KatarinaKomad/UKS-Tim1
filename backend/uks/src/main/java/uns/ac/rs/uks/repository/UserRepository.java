package uns.ac.rs.uks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    List<User> findByIdIn(List<UUID> id);
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllActiveUsers();

}
