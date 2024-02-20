package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.EditRepoRequest;
import uns.ac.rs.uks.dto.request.RepoForkRequest;
import uns.ac.rs.uks.dto.request.RepoRequest;
import uns.ac.rs.uks.dto.request.RepoUpdateRequest;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.exception.NotAllowedException;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.RepoMapper;
import uns.ac.rs.uks.mapper.UserMapper;
import uns.ac.rs.uks.model.Member;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.RepositoryRole;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.repo.RepoRepository;

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
    @Autowired
    private BranchService branchService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private GitoliteService gitoliteService;

//    @Cacheable(value = "repos")
    public List<RepoBasicInfoDTO> getAllPublic() {
        List<Repo> allPublic = repoRepository.findAllByIsPublicTrue();
        return RepoMapper.toDTOs(allPublic);
    }

    @Cacheable(value = "repos", key = "#userID")
    public List<RepoBasicInfoDTO> getMyRepos(UUID userID) {
        List<Repo> allPublic = repoRepository.findAllByOwnerId(userID);
        return RepoMapper.toDTOs(allPublic);
    }

    @CacheEvict(value = "repos", allEntries = true)
    public RepoBasicInfoDTO createNewRepo(RepoRequest repoRequest) throws NotFoundException {
        User user = userService.getById(repoRequest.getOwnerId());
        Repo repo = RepoMapper.toRepoFromRequest(repoRequest, user);
        repo.setDefaultBranch(branchService.createDefaultBranch(repo));
        repoRepository.save(repo);
        memberService.addNewMember(user, repo, RepositoryRole.OWNER);
        var repoDto = RepoMapper.toDTO(repo);
        repoDto.setCloneUri(gitoliteService.createRepo(repoDto.getName(), user.getUsername()));
        return repoDto;
    }

    public Repo getById(UUID repoId) {
        return repoRepository.findById(repoId).orElseThrow(() -> new NotFoundException("Repository not found."));
    }
    public RepoBasicInfoDTO findById(UUID repoId) {
        return RepoMapper.toDTO(getById(repoId));
    }

    public RepoBasicInfoDTO getByNameAndPublicOrMember(RepoRequest repoRequest) {
        List<Repo> repos = repoRepository.findAllByName(repoRequest.getName());
        for (Repo repo : repos) {
            if (repo.getIsPublic()) {
                return RepoMapper.toDTO(repo);
            }
            Member member = memberService.findMemberByUserIdAndRepositoryId(repoRequest.getOwnerId(), repo.getId());
            if (member != null) {
                return RepoMapper.toDTO(repo);
            }
        }
        return null;
    }

    public Boolean canEditRepoItems(EditRepoRequest repoRequest) {
        Member member = memberService.findMemberByUserIdAndRepositoryId(repoRequest.getUserId(),
                repoRequest.getRepoId());
        return member != null;
    }

    public List<UserDTO> getMembers(UUID repoId) {
        List<Member> members = memberService.findAllMembersByRepositoryId(repoId);
        return members.stream().map(member -> UserMapper.toDTO(member.getUser())).toList();
    }

    @CacheEvict(value = "repos", allEntries = true)
    public RepoBasicInfoDTO updateRepo(UUID repositoryId, RepoUpdateRequest request) {
        Repo repo = getById(repositoryId);
        repo.setName(request.getName());
        repo.setDescription(request.getDescription());
        repo.setIsPublic(request.getIsPublic());
        repo.setDefaultBranch(branchService.getById(request.getDefaultBranch()));

        repoRepository.save(repo);
        return RepoMapper.toDTO(repo);
    }

    @CacheEvict(value = "repos", allEntries = true)
    public RepoBasicInfoDTO forkRepo(RepoForkRequest forkRequest) {
        checkForkValidity(forkRequest);
        Repo repo = getById(forkRequest.getOriginalRepoId());
        Repo newRepo = RepoMapper.map(repo);
        newRepo.setOwner(entityManager.getReference(User.class, forkRequest.getOwnerId()));
        newRepo.setName(forkRequest.getName());
        newRepo.setDescription(forkRequest.getDescription());
        newRepo.setForkParent(repo);
        return RepoMapper.toDTO(repoRepository.save(newRepo));
    }

    private void checkForkValidity(RepoForkRequest forkRequest) {
        if(!forkRequest.getIsPublic()){
            Member member = memberService.findMemberByUserIdAndRepositoryId(
                    forkRequest.getOwnerId(), forkRequest.getOriginalRepoId());
            if(member == null) {
                throw new NotAllowedException("Fork not allowed");
            }
        }
    }

    public List<RepoBasicInfoDTO> getAllForked(UUID repoId) {
        Repo repo = getById(repoId);
        return RepoMapper.toDTOs(repo.getForkChildren());
    }
}
