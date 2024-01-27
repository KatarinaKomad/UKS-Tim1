package uns.ac.rs.uks.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.Milestone;
import uns.ac.rs.uks.model.State;
import uns.ac.rs.uks.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class MilestoneRepositoryTest {

    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private EntityManager entityManager;

    @ParameterizedTest(name = "Finding milestones of repository by id {0}")
    @ValueSource(strings = {"a3826e27-77d8-465c-9d9f-87ccbb04ecaf"})
    public void findAllByRepoId(String repoId) {
        UUID id = UUID.fromString(repoId);
        List<Milestone> milestones = milestoneRepository.findAllByRepositoryId(id);
        assertFalse(milestones.isEmpty());
        for (Milestone milestone: milestones) {
            assertEquals(milestone.getRepository().getId(), id);
        }
    }

    @ParameterizedTest(name = "Finding no milestones of repository by id {0}")
    @ValueSource(strings = {"ba6dcc79-1444-4310-9e7d-9736def57f60"})
    public void findAllByOwnerIdNoRepo(String userId) {
        List<Milestone> milestones = milestoneRepository.findAllByRepositoryId(UUID.fromString(userId));
        assertTrue(milestones.isEmpty());
    }

    @Test
    @DisplayName("Test change status of milestone")
    public void updateStatusOfMilestoneToClose() {
        Long milestoneId = 2L;
        State state = State.CLOSE;

        Optional<Milestone> optionalMilestoneBefore = milestoneRepository.findById(milestoneId);
        assertTrue(optionalMilestoneBefore.isPresent());
        assertEquals(optionalMilestoneBefore.get().getState(), State.OPEN);
        milestoneRepository.updateState(milestoneId, state);

        entityManager.flush();
        entityManager.clear();

        Optional<Milestone> optionalMilestoneAfter = milestoneRepository.findById(milestoneId);
        assertTrue(optionalMilestoneAfter.isPresent());
        assertEquals(optionalMilestoneAfter.get().getState(), state);
    }

}
