package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.RepoMapper;
import uns.ac.rs.uks.model.Member;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.RepositoryRole;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.RepoRepository;

import java.util.List;
import java.util.UUID;

@Service
public class RepoService {
    @Autowired
    private RepoRepository repoRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    public List<RepoBasicInfoDTO> getAllPublic() {
        List<Repo> allPublic = repoRepository.findAllByIsPublicTrue();
        return RepoMapper.toDTOs(allPublic);
    }

    public List<RepoBasicInfoDTO> getMyRepos(UUID userID) {
        List<Repo> allPublic = repoRepository.findAllByOwnerId(userID);
        return RepoMapper.toDTOs(allPublic);
    }

    public RepoBasicInfoDTO createNewRepo(RepoRequest repoRequest) throws NotFoundException {
        User user = userService.getById(repoRequest.getOwnerId());
        Repo repo = RepoMapper.toRepoFromRequest(repoRequest, user);
        repo = repoRepository.save(repo);
        memberService.addNewMember(user, repo, RepositoryRole.OWNER);
        return RepoMapper.toDTO(repo);
    }


    public Repo getById(UUID repoId) {
        return repoRepository.findById(repoId).orElseThrow(()->new NotFoundException("Repository not found."));
    }
  
    public RepoBasicInfoDTO getByNameAndPublicOrMember(RepoRequest repoRequest) {
        List<Repo> repos = repoRepository.findAllByName(repoRequest.getName());
        for (Repo repo : repos) {
            if(repo.getIsPublic()){
                return RepoMapper.toDTO(repo);
            }
            Member member = memberService.findMemberByUserIdAndRepositoryId(repoRequest.getOwnerId(), repo.getId());
            if(member != null) {
                return RepoMapper.toDTO(repo);
            }
        }
        return null;
    }
}
