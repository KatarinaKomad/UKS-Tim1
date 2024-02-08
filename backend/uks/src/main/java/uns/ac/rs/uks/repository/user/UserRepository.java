package uns.ac.rs.uks.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uns.ac.rs.uks.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, CustomUserRepository {

    Optional<User> findByEmail(String email);
    Optional<User> findByCustomUsername(String username);
    Optional<User> findById(UUID uuid);

    @Modifying
    @Query("UPDATE User u SET u.blockedByAdmin = ?2 WHERE u.id = ?1")
    void updateBlockedByAdmin(UUID id, boolean blocked);

    @Modifying
    @Query("UPDATE User u SET u.deleted = ?2 WHERE u.id = ?1")
    void updateDeletedByAdmin(UUID id, boolean deleted);
}
