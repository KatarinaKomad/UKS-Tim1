package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.mapper.RepoMapper;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.repository.RepoRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RepoService {
    @Autowired
    private RepoRepository repoRepository;

    @Cacheable(value = "repos")
    public List<RepoBasicInfoDTO> getAllPublic() {
        List<Repo> allPublic = repoRepository.findAllByIsPublicTrue();
        return RepoMapper.toDTOs(allPublic);
    }

    @Cacheable(value = "repos", key = "#userID")
    public List<RepoBasicInfoDTO> getMyRepos(UUID userID) {
        List<Repo> allPublic = repoRepository.findAllByOwnerId(userID);
        return RepoMapper.toDTOs(allPublic);
    }
}
