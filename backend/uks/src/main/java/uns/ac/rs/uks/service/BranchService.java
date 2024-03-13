package uns.ac.rs.uks.service;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.OriginTargetBranchRequest;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.dto.response.CommitsResponseDto;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.BranchMapper;
import uns.ac.rs.uks.model.Branch;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.BranchRepository;
import uns.ac.rs.uks.repository.repo.RepoRepository;

import java.util.List;
import java.util.UUID;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RepoRepository repoRepository;
    @Autowired
    private GitoliteService gitoliteService;
    @Autowired
    private EntityManager entityManager;

    public Branch createDefaultBranch(Repo repo, User creator) {
        Branch branch = new Branch();
        branch.setName("master");
        branch.setRepository(repo);
        branch.setUpdatedBy(creator);
        branchRepository.save(branch);
        return branch;
    }

    public  Branch getById(Long id) {
        return  branchRepository.findById(id).orElseThrow(()->new NotFoundException("Branch not found."));
    }

    public List<BranchDTO> getRepoBranches(UUID repoId) {
        var repo = getRepoById(repoId);
        List<BranchDTO> gitoliteBranches = gitoliteService.readRepoBranches(formatRepoName(repo.getName()));
        // TODO: Save gitolite branch to repo if not saved
        List<Branch> branches = branchRepository.findAllByRepositoryId(repoId);
        return BranchMapper.toDTOs(branches);
    }
    public Long getRepoBranchesCount(UUID repoId) {
        return branchRepository.countAllByRepositoryId(repoId);
    }

    public List<CommitsResponseDto> getCommits(UUID repoId, String branch){
        var repo = getRepoById(repoId);
        return gitoliteService.getBranchCommits(formatRepoName(repo.getName()), branch);
    }

    public String getDifferences(UUID repoId, String originBranch, String destinationBranch) {
        var repo = getRepoById(repoId);
        return gitoliteService.getDifferences(formatRepoName(repo.getName()), originBranch, destinationBranch);
    }

    public void mergeBranches(OriginTargetBranchRequest request, User user) {
        var repo = getRepoById(request.getRepoId());

        updateBranch(request.getRepoId(), request.getOriginName(), user);
        updateBranch(request.getRepoId(), request.getTargetName(), user);

        gitoliteService.mergeBranches(formatRepoName(repo.getName()), request.getOriginName(), request.getTargetName());
    }


    public void deleteBranch(UUID repoId, String branchName){
        var repo = getRepoById(repoId);
        gitoliteService.deleteBranch(formatRepoName(repo.getName()), branchName);
        branchRepository.deleteByRepositoryIdAndName(repoId, branchName);
    }

    public BranchDTO newBranch(OriginTargetBranchRequest request, User user) {
        User repoUser = entityManager.getReference(User.class, user.getId());
        var repo = getRepoById(request.getRepoId());
        gitoliteService.newBranch(formatRepoName(repo.getName()), request.getOriginName(), request.getTargetName());
        Branch newBranch = Branch.builder().name(request.getTargetName()).repository(repo).updatedBy(repoUser).build();
        return BranchMapper.toDTO(branchRepository.save(newBranch));
    }

    private Branch updateBranch(UUID repoId, String branchName, User user) {
        Branch branch = branchRepository.findByRepositoryIdAndName(repoId, branchName);
        branch.setUpdatedBy(user);
        return branchRepository.save(branch);
    }

    public BranchDTO renameBranch(OriginTargetBranchRequest request, User user) {
        User repoUser = entityManager.getReference(User.class, user.getId());
        Repo repo = getRepoById(request.getRepoId());

        Branch branch = updateBranch(repo.getId(), request.getOriginName(), repoUser);
        branch.setName(request.getTargetName());

        gitoliteService.renameBranch(repo.getName(), request.getOriginName(), request.getTargetName());
        return BranchMapper.toDTO(branchRepository.save(branch));
    }

    private String formatRepoName(String name) {
        return name.replace(" ", "-");
    }

    private Repo getRepoById(UUID id) {
        return repoRepository.findById(id).orElseThrow(() -> new NotFoundException("Repo not found"));
    }
}
