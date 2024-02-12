package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.BranchRequest;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.BranchMapper;
import uns.ac.rs.uks.model.Branch;
import uns.ac.rs.uks.model.Repo;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.BranchRepository;
import java.util.List;
import java.util.UUID;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private RepoService repoService;
    @Autowired
    private UserService userService;

    public Branch getById(Long branchId) {
        return branchRepository.findById(branchId).orElseThrow(()->new NotFoundException("Branch not found."));
    }

    public List<BranchDTO> getAllRepoBranches(UUID repoId) {
        List<Branch> branches = branchRepository.findAllByRepositoryId(repoId);
        return BranchMapper.toDTOs(branches);
    }

    public BranchDTO createNewBranch(BranchRequest branchRequest) throws NotFoundException {
        Repo repo = repoService.getById(branchRequest.getRepoId());
        User user = userService.getById(branchRequest.getUpdatedBy().getId());
        Branch branch = BranchMapper.fromDTO(branchRequest, repo, user);
        branch = branchRepository.save(branch);
        return BranchMapper.toDTO(branch);
    }

    public BranchDTO editBranchName(BranchRequest branchRequest) throws NotFoundException {
        Branch branch = getById(branchRequest.getId());
        BranchMapper.map(branch, branchRequest);
        User user = userService.getById(branchRequest.getUpdatedBy().getId());
        branch.setUpdatedBy(user);
        branch = branchRepository.save(branch);
        return BranchMapper.toDTO(branch);
    }

    public void deleteBranch(Long branchId) {
        branchRepository.deleteById(branchId);
    }
}
