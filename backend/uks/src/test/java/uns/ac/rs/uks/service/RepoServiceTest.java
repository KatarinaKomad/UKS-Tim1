package uns.ac.rs.uks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.Role;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.RepoRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RepoServiceTest {
    @InjectMocks
    private RepoService repoService;
    @Mock
    private RepoRepository repoRepository;
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
    void testGetPublicRepos() {
        Repo repo1 = new Repo();
        repo1.setIsPublic(true);
        Repo repo2 = new Repo();
        repo2.setIsPublic(true);

        when(repoRepository.findAllByIsPublicTrue()).thenReturn(List.of(repo1, repo2));

        List<RepoBasicInfoDTO> publicRepos = repoService.getAllPublic();
        assertEquals(publicRepos.size(), 2);
        assertTrue(publicRepos.get(0).getIsPublic());
        assertTrue(publicRepos.get(1).getIsPublic());
    }


    @ParameterizedTest(name = "Finding repositories owned by user with id {0}")
    @ValueSource(strings = {"0e7f2a1d-49d0-44cd-8a01-4d40186f6f08", "ff1d6606-e1f5-4e26-8a32-a14800b42a27"})
    void testGetMyRepos(String userId) {
        UUID id = UUID.fromString(userId);

        User owner = new User();
        owner.setId(id);
        Repo repo = new Repo();
        repo.setOwner(owner);

        when(repoRepository.findAllByOwnerId(id)).thenReturn(List.of(repo));

        List<RepoBasicInfoDTO> myRepos = repoService.getMyRepos(id);
        assertFalse(myRepos.isEmpty());
        assertEquals(myRepos.get(0).getOwner().getId(), id);
    }


    @ParameterizedTest(name = "Finding no repositories owned by user with id {0}")
    @ValueSource(strings = {"af409c2d-95e0-432e-a6fc-6ef55cb4430d"})
    void testGetMyReposNoRepos(String userId) {
        UUID id = UUID.fromString(userId);

        when(repoRepository.findAllByOwnerId(id)).thenReturn(new ArrayList<>());

        List<RepoBasicInfoDTO> myRepos = repoService.getMyRepos(id);
        assertTrue(myRepos.isEmpty());
    }

}
