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
import uns.ac.rs.uks.dto.request.LabelRequest;
import uns.ac.rs.uks.dto.response.LabelDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.Label;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.repository.label.LabelRepository;
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
public class LabelServiceTest {

    @InjectMocks
    private LabelService labelService;
    @Mock
    private LabelRepository labelRepository;
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
    void testGetLabelByIdSuccess() {
        // Mocking
        Long id = 1L;
        Label label = new Label();
        label.setId(id);
        when(labelRepository.findById(id)).thenReturn(Optional.of(label));
        // Test
        Label label1 = labelService.getById(id);
        // Assertions
        assertNotNull(label1);
        assertEquals(label1.getId(), id);
    }

    @Test
    public void testNoLabelById() {
        // Mocking
        when(labelRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // Test && Assertions
        assertThrows(NotFoundException.class, () -> labelService.getById(1L));
    }

    @Test
    void testGetAllRepoLabels() {
        Repo repository = new Repo();
        repository.setId(Constants.REPOSITORY_ID_1_UKS_TEST);
        Label label1 = new Label();
        label1.setRepository(repository);
        Label label2 = new Label();
        label2.setRepository(repository);

        when(labelRepository.findAllByRepositoryId(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(List.of(label1, label2));

        List<LabelDTO> publicRepos = labelService.getAllRepoLabels(Constants.REPOSITORY_ID_1_UKS_TEST);
        assertEquals(publicRepos.size(), 2);
        assertEquals(publicRepos.get(0).getRepoId(), Constants.REPOSITORY_ID_1_UKS_TEST);
        assertEquals(publicRepos.get(1).getRepoId(), Constants.REPOSITORY_ID_1_UKS_TEST);
    }

    @Test
    public void testCreateNewLabel() throws NotFoundException {
        // Mocking
        Repo repo = createRepo("testName", Constants.REPOSITORY_ID_1_UKS_TEST);

        LabelRequest labelRequest = createLabelRequest(Constants.REPOSITORY_ID_1_UKS_TEST);

        Label label = new Label();
        label.setColor("#118d56");
        label.setDescription("test description3");
        label.setName("test name3");
        label.setRepository(repo);

        when(repoService.getById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenReturn(repo);
        when(labelRepository.save(any(Label.class))).thenReturn(label);

        // Test
        LabelDTO dto = labelService.createNewLabel(labelRequest);

        // Assertions
        assertNotNull(dto);
        assertEquals(Constants.REPOSITORY_ID_1_UKS_TEST, dto.getRepoId());
        assertEquals("test name3", dto.getName());
    }

    @Test
    void testAddNewRepoUserDoesNotExists(){
        // Mocking
        LabelRequest labelRequest = createLabelRequest(Constants.REPOSITORY_ID_1_UKS_TEST);

        when(repoService.getById(Constants.REPOSITORY_ID_1_UKS_TEST)).thenThrow(new NotFoundException("User not found!"));

        // Test && Assertions
        assertThrows(NotFoundException.class, () -> labelService.createNewLabel(labelRequest));
    }


    @Test
    public void testEditLabel() throws NotFoundException {
        // Mocking

        LabelRequest labelRequest = createLabelRequest(Constants.REPOSITORY_ID_1_UKS_TEST);
        labelRequest.setId(1L);

        Label label = new Label();
        label.setId(1L);
        label.setColor("#118d56");
        label.setDescription("test description3");
        label.setName("test name3");

        when(labelRepository.save(any(Label.class))).thenReturn(label);
        when(labelRepository.findById(1L)).thenReturn(Optional.of(label));

        // Test
        LabelDTO dto = labelService.editLabel(labelRequest);

        // Assertions
        assertNotNull(dto);
        assertEquals(labelRequest.getName(), label.getName());
        assertEquals(labelRequest.getDescription(), label.getDescription());
        assertEquals(labelRequest.getColor(), label.getColor());
    }

    private LabelRequest createLabelRequest(UUID repoId) {
        LabelRequest labelRequest = new LabelRequest();
        labelRequest.setColor("#118d56");
        labelRequest.setDescription("test description3");
        labelRequest.setName("test name3");
        labelRequest.setRepoId(repoId);
        return labelRequest;
    }

    private Repo createRepo(String name, UUID id) {
        Repo repo = new Repo();
        repo.setName(name);
        repo.setId(id);
        repo.setIsPublic(true);
        return repo;
    }

}
