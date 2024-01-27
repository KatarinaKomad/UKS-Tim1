package uns.ac.rs.uks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.request.MilestoneRequest;
import uns.ac.rs.uks.dto.response.MilestoneDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.Milestone;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.repository.MilestoneRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MilestoneServiceTest {

    @InjectMocks
    private MilestoneService milestoneService;
    @Mock
    private MilestoneRepository milestoneRepository;
    @Mock
    private RepoService repoService;
    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void testGetMilestoneByIdSuccess() {
        // Mocking
        Long id = 1L;
        Milestone milestone = new Milestone();
        milestone.setId(id);
        when(milestoneRepository.findById(id)).thenReturn(Optional.of(milestone));
        // Test
        Milestone milestone1 = milestoneService.getById(id);
        // Assertions
        assertNotNull(milestone1);
        assertEquals(milestone1.getId(), id);
    }

    @Test
    public void testNoMilestoneById() {
        // Mocking
        when(milestoneRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> milestoneService.getById(1L));
    }

    @Test
    void testGetAllRepoMilestones() {
        Repo repository = new Repo();
        repository.setId(Constants.REPOSITORY_ID_1_UKS_TEST);
        Milestone milestone1 = new Milestone();
        milestone1.setRepository(repository);
        Milestone milestone2 = new Milestone();
        milestone2.setRepository(repository);

        when(milestoneRepository.findAllByRepositoryId(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(List.of(milestone1, milestone2));

        List<MilestoneDTO> publicRepos = milestoneService.getAllRepoMilestones(Constants.REPOSITORY_ID_1_UKS_TEST);
        assertEquals(publicRepos.size(), 2);
        assertEquals(publicRepos.get(0).getRepoId(), Constants.REPOSITORY_ID_1_UKS_TEST);
        assertEquals(publicRepos.get(1).getRepoId(), Constants.REPOSITORY_ID_1_UKS_TEST);
    }

    @Test
    public void testCreateNewMilestone() throws NotFoundException {
        // Mocking
        Repo repo = createRepo("testName", Constants.REPOSITORY_ID_1_UKS_TEST);

        MilestoneRequest milestoneRequest = createMilestoneRequest(Constants.REPOSITORY_ID_1_UKS_TEST);

        Milestone milestone = new Milestone();
        milestone.setDescription("test description3");
        milestone.setName("test name3");
        milestone.setRepository(repo);

        when(repoService.getById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(repo);
        when(milestoneRepository.save(any(Milestone.class))).thenReturn(milestone);

        // Test
        MilestoneDTO dto = milestoneService.createNewMilestone(milestoneRequest);

        // Assertions
        assertNotNull(dto);
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, dto.getRepoId());
        assertEquals("test name3", dto.getName());
    }

    @Test
    void testAddNewRepoUserDoesNotExists(){
        // Mocking
        MilestoneRequest milestoneRequest = createMilestoneRequest(Constants.REPOSITORY_ID_1_UKS_TEST);

        when(repoService.getById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenThrow(new NotFoundException("User not found!"));

        // Test && Assertions
        assertThrows(NotFoundException.class, () -> milestoneService.createNewMilestone(milestoneRequest));
    }


    @Test
    public void testEditMilestone() throws NotFoundException {
        // Mocking

        MilestoneRequest milestoneRequest = createMilestoneRequest(Constants.REPOSITORY_ID_1_UKS_TEST);
        milestoneRequest.setId(1L);

        Milestone milestone = new Milestone();
        milestone.setId(1L);
        milestone.setDescription("test description3");
        milestone.setName("test name3");

        when(milestoneRepository.save(any(Milestone.class))).thenReturn(milestone);
        when(milestoneRepository.findById(1L)).thenReturn(Optional.of(milestone));

        // Test
        MilestoneDTO dto = milestoneService.editMilestone(milestoneRequest);

        // Assertions
        assertNotNull(dto);
        assertEquals(milestoneRequest.getName(), milestone.getName());
        assertEquals(milestoneRequest.getDescription(), milestone.getDescription());
    }

    private MilestoneRequest createMilestoneRequest(UUID repoId) {
        MilestoneRequest milestoneRequest = new MilestoneRequest();
        milestoneRequest.setDescription("test description3");
        milestoneRequest.setName("test name3");
        milestoneRequest.setRepoId(repoId);
        return milestoneRequest;
    }

    private Repo createRepo(String name, UUID id) {
        Repo repo = new Repo();
        repo.setName(name);
        repo.setId(id);
        repo.setIsPublic(true);
        return repo;
    }

}
