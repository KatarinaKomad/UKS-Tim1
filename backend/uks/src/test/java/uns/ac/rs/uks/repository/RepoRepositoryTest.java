package uns.ac.rs.uks.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.Repo;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class RepoRepositoryTest {

    @Autowired
    private RepoRepository repoRepository;
    @Test
    public void findAllByIsPublicTrue() {
        List<Repo> publicRepos = repoRepository.findAllByIsPublicTrue();
        assertFalse(publicRepos.isEmpty());
        for (Repo repo: publicRepos) {
            assertTrue(repo.getIsPublic());
        }
    }
    @ParameterizedTest(name = "Finding repositories owned by user with id {0}")
    @ValueSource(strings = {"0e7f2a1d-49d0-44cd-8a01-4d40186f6f08", "ff1d6606-e1f5-4e26-8a32-a14800b42a27"})
    public void findAllByOwnerId(String userId) {
        UUID id = UUID.fromString(userId);
        List<Repo> publicRepos = repoRepository.findAllByOwnerId(id);
        assertFalse(publicRepos.isEmpty());
        for (Repo repo: publicRepos) {
            assertEquals(repo.getOwner().getId(), id);
        }
    }
    @ParameterizedTest(name = "Finding no repositories owned by user with id {0}")
    @ValueSource(strings = {"af409c2d-95e0-432e-a6fc-6ef55cb4430d"})
    public void findAllByOwnerIdNoRepo(String userId) {
        List<Repo> publicRepos = repoRepository.findAllByOwnerId(UUID.fromString(userId));
        assertTrue(publicRepos.isEmpty());
    }

}
