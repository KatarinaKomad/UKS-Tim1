package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.*;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.dto.response.RepoBasicInfoDTO;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.dto.response.WatchStarResponseDTO;
import uns.ac.rs.uks.exception.NotAllowedException;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.BranchMapper;
import uns.ac.rs.uks.mapper.RepoMapper;
import uns.ac.rs.uks.mapper.UserMapper;
import uns.ac.rs.uks.model.*;
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
        repo.setDefaultBranch(branchService.createDefaultBranch(repo, user));

        repo.setCloneUri(gitoliteService.createRepo(repo.getName(), user.getUsername()));
        memberService.addNewMember(user, repo, RepositoryRole.OWNER, MemberInviteStatus.ACCEPTED);

        return RepoMapper.toDTO(repoRepository.save(repo));
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
        User owner = entityManager.getReference(User.class, forkRequest.getOwnerId());
        Repo repo = getById(forkRequest.getOriginalRepoId());

        Repo newRepo = RepoMapper.map(repo);
        newRepo.setOwner(owner);
        newRepo.setName(forkRequest.getName());
        newRepo.setDescription(forkRequest.getDescription());
        newRepo.setForkParent(repo);
        newRepo.setCloneUri(gitoliteService.createRepo(newRepo.getName(), owner.getUsername()));

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

    public RepoBasicInfoDTO starRepo(RepoUserRequest starRequest) {
        Repo repo = getById(starRequest.getRepoId());
        User user = userService.getById(starRequest.getUserId());
        boolean isStargazing = repo.getStaredBy().stream().anyMatch(u -> u.getId().equals(user.getId()));
        if(!isStargazing) {
            user.getStared().add(repo);
            repo.getStaredBy().add(user);
        } else {
            user.getStared().remove(repo);
            repo.getStaredBy().remove(user);
        }
        return RepoMapper.toDTO(repoRepository.save(repo));
    }

    public RepoBasicInfoDTO watchRepo(RepoUserRequest starRequest) {
        Repo repo = getById(starRequest.getRepoId());
        User user = userService.getById(starRequest.getUserId());
        boolean isWatching = repo.getWatchers().stream().anyMatch(u -> u.getId().equals(starRequest.getUserId()));
        if(!isWatching) {
            user.getWatching().add(repo);
            repo.getWatchers().add(user);
        } else {
            user.getWatching().remove(repo);
            repo.getWatchers().remove(user);
        }
        return RepoMapper.toDTO(repoRepository.save(repo));
    }

    public List<UserDTO> getAllStargazers(UUID repoId) {
        Repo repo = getById(repoId);
        return UserMapper.toDTOs(repo.getStaredBy());
    }

    public List<UserDTO> getAllWatchers(UUID repoId) {
        Repo repo = getById(repoId);
        return UserMapper.toDTOs(repo.getWatchers());
    }

    public WatchStarResponseDTO amIWatchingStargazing(RepoUserRequest request) {
        Repo repo = getById(request.getRepoId());
        boolean isWatching = repo.getWatchers().stream().anyMatch(u -> u.getId().equals(request.getUserId()));
        boolean isStargazing = repo.getStaredBy().stream().anyMatch(u -> u.getId().equals(request.getUserId()));
        return new WatchStarResponseDTO(isWatching, isStargazing);
    }

    @CacheEvict(value = "repos", allEntries = true)
    public void deleteRepo(UUID repoId) {
        memberService.removeAllMembersFromRepo(repoId);
        repoRepository.deleteById(repoId);
    }

    public BranchDTO getDefaultBranch(UUID repoId) {
        Repo repo = getById(repoId);
        return BranchMapper.toDTO(repo.getDefaultBranch());
    }
}
